package com.tegik.api.lambda.config;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.json.simple.parser.ParseException;

import com.tegik.api.lambda.annotations.Consumes;
import com.tegik.api.lambda.annotations.ContentType;
import com.tegik.api.lambda.annotations.DELETE;
import com.tegik.api.lambda.annotations.GET;
import com.tegik.api.lambda.annotations.POST;
import com.tegik.api.lambda.annotations.PUT;
import com.tegik.api.lambda.annotations.Path;
import com.tegik.api.lambda.annotations.PathParam;
import com.tegik.api.lambda.annotations.QueryParam;
import com.tegik.api.lambda.aws.AWSRequestAPI;

public abstract class Configuration {

	private AWSRequestAPI awsRequestApi;

	public abstract Set<Class<?>> getClasses();

	public void execute(InputStream inputStream, OutputStream outputStream) throws IOException, ParseException {

		String jsonRequest = toString(inputStream);

		awsRequestApi = AWSRequestAPI.build(jsonRequest);
		Set<Class<?>> objs = getClasses();

		String[] endpoints = awsRequestApi.getContext().getResourcePath().substring(1).split("/");

		for (Class<?> obj : objs) {
			if (obj.isAnnotationPresent(Path.class)) {
				Annotation pathAnnotation = obj.getAnnotation(Path.class);
				Path path = (Path) pathAnnotation;
				String valuePath = path.value();

				int cont = 0;
				String path_find = endpoints[cont];
				while (!valuePath.equals(path_find) && cont < endpoints.length - 1) {
					cont += 1;
					path_find = path_find + "/" + endpoints[cont];
				}
				cont += 1;
				if (valuePath.equals(path_find)) {
					reviewMethods(obj, endpoints, cont);
				}

			}

		}

	}

	private void reviewMethods(Class<?> obj, String endpoints[], int cont) {
	

		for (Method method : obj.getDeclaredMethods()) {
			
			//There is a Consumes annotation and specify the content type received
			Consumes cons = null;
			if (method.isAnnotationPresent(Consumes.class)) {
				cons = method.getAnnotation(Consumes.class);
			}

			if (method.isAnnotationPresent(GET.class) && awsRequestApi.getContext().getHttpMethod().equals("GET")) {
				invokeMethod(obj, method, endpoints, cont, cons);

			} else if (method.isAnnotationPresent(POST.class)
					&& awsRequestApi.getContext().getHttpMethod().equals("POST")) {
				invokeMethod(obj, method, endpoints, cont, cons);

			} else if (method.isAnnotationPresent(PUT.class)
					&& awsRequestApi.getContext().getHttpMethod().equals("PUT")) {

				invokeMethod(obj, method, endpoints, cont, cons);

			} else if (method.isAnnotationPresent(DELETE.class)
					&& awsRequestApi.getContext().getHttpMethod().equals("DELETE")) {
				invokeMethod(obj, method, endpoints, cont, cons);

			}
		}

	}

	private Object[] getParameters(Method method, Consumes cons) {
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		Class<?>[] parametersClass = method.getParameterTypes();
		List<Object> listaObjects = new ArrayList<Object>();

		for (int x = 0; x < parameterAnnotations.length; x++) {

			Class<?> parameterClass = parametersClass[x];

			// there are not annotations
			if (parameterAnnotations[x].length == 0) {
				listaObjects.add(
						transformConsumes(parameterClass.getName(), awsRequestApi.getBodyJson().toJSONString(), cons));
			}

			// there are annotations
			for (Annotation annotation : parameterAnnotations[x]) {

				// Annotation QueryPath
				if (annotation instanceof QueryParam) {
					QueryParam query = (QueryParam) annotation;

					if (awsRequestApi.getParams().getQuerystring().containsKey(query.value())) {
						String valueQueryRequest = awsRequestApi.getParams().getQuerystring().get(query.value());
						listaObjects.add(transform(parameterClass.getName(), valueQueryRequest));
					} else {
						listaObjects.add(null);
					}

				}
				// Annotation PathParam
				else if (annotation instanceof PathParam) {
					PathParam pathParam = (PathParam) annotation;

					if (awsRequestApi.getParams().getPath().containsKey(pathParam.value())) {
						String valuePathRequest = awsRequestApi.getParams().getPath().get(pathParam.value());
						listaObjects.add(transform(parameterClass.getName(), valuePathRequest));
					} else {
						listaObjects.add(null);
					}

				}

			}
		}

		return listaObjects.toArray(new Object[listaObjects.size()]);
	}

	private void invokeMethod(Class<?> obj, Method method, String endpoints[], int cont, Consumes cons) {
		Object[] objects = getParameters(method, cons);

		try {
			if (method.isAnnotationPresent(Path.class) && endpoints.length > cont) {

				Annotation pathAnnotation = (Path) method.getAnnotation(Path.class);

				Path path = (Path) pathAnnotation;
				String valuePath = path.value();
				String valueFind = endpoints[cont];

				if (valueFind.equals(path.value())) {
					if (objects != null)
						method.invoke(obj.newInstance(), objects);
				}

				cont += 1;
				for (int i = cont; i < endpoints.length; i++) {

					valueFind = valueFind + "/" + endpoints[i];
					if (valueFind.equals(path.value())) {

						if (objects != null)
							method.invoke(obj.newInstance(), objects);

					}
				}

			} else if (!method.isAnnotationPresent(Path.class) && endpoints.length <= cont) {
				if (objects != null)
					method.invoke(obj.newInstance(), objects);
				else
					method.invoke(obj.newInstance());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Object transform(String objClass, String value) {

		if (objClass.equals("java.lang.String")) {
			return new String(value);
		} else
			return null;
	}

	private Object transformConsumes(String objClass, String value, Consumes cons) {

		if (cons == null) {
			return null;
		}
		for (int i = 0; i < cons.value().length; i++) {
			if (objClass.equals("java.lang.String")
					&& awsRequestApi.getParams().getHeader().get("Content-Type").equals("application/json")
					&& cons.value()[i] == ContentType.APPLICATION_JSON) {
				return new String(value);
			}
		}

		return null;
	}

	private String toString(InputStream in) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String read;

		while ((read = br.readLine()) != null) {
			sb.append(read);
		}

		br.close();
		return sb.toString();
	}

	public static void main(String args[]) {
		String readFile;
		try {
			readFile = new Scanner(new File("./src/main/resources/json"), "UTF-8").useDelimiter("\\A").next();

			InputStream inputStream = new ByteArrayInputStream(readFile.getBytes(StandardCharsets.UTF_8));
			Configuration config = new ConfigurationExample();
			config.execute(inputStream, new FileOutputStream("./src/main/resources/respuesta.json"));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

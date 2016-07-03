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

import com.tegik.api.lambda.annotations.DELETE;
import com.tegik.api.lambda.annotations.GET;
import com.tegik.api.lambda.annotations.POST;
import com.tegik.api.lambda.annotations.PUT;
import com.tegik.api.lambda.annotations.Path;
import com.tegik.api.lambda.annotations.Query;
import com.tegik.api.lambda.aws.AWSRequestAPI;

public abstract class Configuration {

	public abstract Set<Class<?>> getClasses();

	public void execute(InputStream inputStream, OutputStream outputStream) throws IOException, ParseException {

		String jsonRequest = toString(inputStream);

		AWSRequestAPI awsRequestApi = AWSRequestAPI.build(jsonRequest);
		Set<Class<?>> objs = getClasses();

		String[] endpoints = awsRequestApi.getContext().getResourcePath().substring(1).split("/");

		for (Class<?> obj : objs) {
			if (obj.isAnnotationPresent(Path.class)) {
				Annotation pathAnnotation = obj.getAnnotation(Path.class);
				Path path = (Path) pathAnnotation;
				String valuePath = path.value();

				int cont = 0;
				String path_find = endpoints[cont];
				System.out.println("path_find: "+ path_find + " valuePath: " + valuePath);
				while (!valuePath.equals(path_find) && cont < endpoints.length-1) {
					cont += 1;
					path_find = path_find + "/" + endpoints[cont];
				}
				if(valuePath.equals(path_find)){
					reviewMethods(obj, awsRequestApi);
				}

			}

		}

	}

	private void reviewMethods(Class<?> obj, AWSRequestAPI awsRequestApi) {

		for (Method method : obj.getDeclaredMethods()) {

			if (method.isAnnotationPresent(GET.class) && awsRequestApi.getContext().getHttpMethod().equals("GET")) {

				Annotation getAnnotation = obj.getAnnotation(GET.class);
				GET get = (GET) getAnnotation;
				Object[] objects = getParameters(method, awsRequestApi);

				try {
					if (objects != null)
						method.invoke(obj.newInstance(), objects);
					else
						method.invoke(obj.newInstance());
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (method.isAnnotationPresent(POST.class)
					&& awsRequestApi.getContext().getHttpMethod().equals("POST")) {

				Annotation getAnnotation = obj.getAnnotation(POST.class);
				POST post = (POST) getAnnotation;
				Object[] objects = getParameters(method, awsRequestApi);

				try {
					if (objects != null)
						method.invoke(obj.newInstance(), objects);
					else
						method.invoke(obj.newInstance());
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (method.isAnnotationPresent(PUT.class)
					&& awsRequestApi.getContext().getHttpMethod().equals("PUT")) {

				Annotation getAnnotation = obj.getAnnotation(PUT.class);
				PUT put = (PUT) getAnnotation;
				Object[] objects = getParameters(method, awsRequestApi);

				try {
					if (objects != null)
						method.invoke(obj.newInstance(), objects);
					else
						method.invoke(obj.newInstance());
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (method.isAnnotationPresent(DELETE.class)
					&& awsRequestApi.getContext().getHttpMethod().equals("DELETE")) {

				Annotation getAnnotation = obj.getAnnotation(GET.class);
				DELETE delete = (DELETE) getAnnotation;
				Object[] objects = getParameters(method, awsRequestApi);

				try {
					if (objects != null)
						method.invoke(obj.newInstance(), objects);
					else
						method.invoke(obj.newInstance());
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}

	}

	private Object[] getParameters(Method method, AWSRequestAPI awsRequestApi) {
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		Class<?>[] parameters = method.getParameterTypes();
		List<Object> listaObjects = new ArrayList<Object>();

		for (int x = 0; x < parameterAnnotations.length; x++) {
			Class<?> parameter = parameters[x];

			for (Annotation annotation : parameterAnnotations[x]) {

				if (annotation instanceof Query) {
					Query query = (Query) annotation;

					if (awsRequestApi.getParams().getQuerystring().containsKey(query.value())) {
						String valueQueryRequest = awsRequestApi.getParams().getQuerystring().get(query.value());
						listaObjects.add(transform(parameter.getName(), valueQueryRequest));
					} else {
						listaObjects.add(null);
					}

				}

			}
		}

		return listaObjects.toArray(new Object[listaObjects.size()]);
	}

	private Object transform(String objClass, String value) {

		if (objClass.equals("java.lang.String")) {
			return new String(value);
		} else
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

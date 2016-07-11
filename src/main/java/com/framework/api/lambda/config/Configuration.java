package com.framework.api.lambda.config;

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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.parser.ParseException;

import com.framework.api.lambda.annotations.Consumes;
import com.framework.api.lambda.annotations.ContentType;
import com.framework.api.lambda.annotations.DELETE;
import com.framework.api.lambda.annotations.GET;
import com.framework.api.lambda.annotations.POST;
import com.framework.api.lambda.annotations.PUT;
import com.framework.api.lambda.annotations.Path;
import com.framework.api.lambda.annotations.PathParam;
import com.framework.api.lambda.annotations.Produces;
import com.framework.api.lambda.annotations.QueryParam;
import com.framework.api.lambda.annotations.StageVariable;
import com.framework.api.lambda.aws.AWSRequestAPI;
import com.framework.api.lambda.test.ConfigurationExample;

public abstract class Configuration {

	private AWSRequestAPI awsRequestApi;
	private OutputStream outputStream;

	public abstract Set<Class<?>> getClasses();

	public void execute(InputStream inputStream, OutputStream outputStream) throws Exception {
		String jsonRequest = toString(inputStream);
		awsRequestApi = AWSRequestAPI.build(jsonRequest);
		this.outputStream=outputStream;
		execute(awsRequestApi, outputStream);

	}

	public void execute(AWSRequestAPI awsRequestApi, OutputStream outputStream)
			throws InstantiationException, IllegalAccessException {
		this.outputStream = outputStream;
		Set<Class<?>> objs = getClasses();
		this.awsRequestApi = awsRequestApi;

		String[] endpoints = awsRequestApi.getContext().getResourcePath().substring(1).split("/");

		for (Class<?> obj : objs) {
			Object newInstance = obj.newInstance();
			if (obj.isAnnotationPresent(Path.class)) {

				Annotation pathAnnotation = obj.getAnnotation(Path.class);
				Path path = (Path) pathAnnotation;
				String valuePath = path.value();

				Field[] fields = obj.getFields();
				for (int f = 0; f < fields.length; f++) {
					if (fields[f].isAnnotationPresent(StageVariable.class)) {
						Annotation fieldAnnotation = fields[f].getAnnotation(StageVariable.class);
						StageVariable stageVariable = (StageVariable) fieldAnnotation;
						String nameStageVariable = stageVariable.value();

						if (awsRequestApi.getStageVariables().containsKey(nameStageVariable)) {
							String valueStageVariable = awsRequestApi.getStageVariables().get(nameStageVariable);
							fields[f].set(newInstance, valueStageVariable);
						}
					}
				}

				int cont = 0;
				String path_find = endpoints[cont];
				while (!valuePath.equals(path_find) && cont < endpoints.length - 1) {
					cont += 1;
					path_find = path_find + "/" + endpoints[cont];
				}
				cont += 1;
				if (valuePath.equals(path_find)) {
					reviewMethods(obj, newInstance, endpoints, cont);
				}

			}

		}

	}

	private void reviewMethods(Class<?> obj, Object newInstance, String endpoints[], int cont) {

		for (Method method : obj.getDeclaredMethods()) {

			// There is a Consumes annotation and specify the content type
			// received
			Consumes cons = null;
			if (method.isAnnotationPresent(Consumes.class)) {
				cons = method.getAnnotation(Consumes.class);
			}

			if (method.isAnnotationPresent(GET.class) && awsRequestApi.getContext().getHttpMethod().equals("GET")) {
				invokeMethod(newInstance, method, endpoints, cont, cons);

			} else if (method.isAnnotationPresent(POST.class)
					&& awsRequestApi.getContext().getHttpMethod().equals("POST")) {
				invokeMethod(newInstance, method, endpoints, cont, cons);

			} else if (method.isAnnotationPresent(PUT.class)
					&& awsRequestApi.getContext().getHttpMethod().equals("PUT")) {

				invokeMethod(newInstance, method, endpoints, cont, cons);

			} else if (method.isAnnotationPresent(DELETE.class)
					&& awsRequestApi.getContext().getHttpMethod().equals("DELETE")) {
				invokeMethod(newInstance, method, endpoints, cont, cons);

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
				listaObjects.add(transformConsumes(parameterClass.getName(), awsRequestApi.getBodyJson(), cons));
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

	private void invokeMethod(Object newInstance, Method method, String endpoints[], int cont, Consumes cons) {
		Object[] objects = getParameters(method, cons);

		try {
			if (method.isAnnotationPresent(Path.class) && endpoints.length > cont) {

				Annotation pathAnnotation = (Path) method.getAnnotation(Path.class);

				Path path = (Path) pathAnnotation;
				String valuePath = path.value();
				String valueFind = endpoints[cont];

				// finding the path correctly at the first endpoint
				if (valueFind.equals(path.value())) {
					if (method.isAnnotationPresent(Produces.class)) {

						Annotation producesAnnotation = method.getAnnotation(Produces.class);
						Produces produces = (Produces) producesAnnotation;

						Object returnObject = method.invoke(newInstance, objects);
						transformProduces(returnObject, produces);
					} else {
						method.invoke(newInstance, objects);
					}
				}

				// finding the path correctly at the loop
				cont += 1;
				for (int i = cont; i < endpoints.length; i++) {

					valueFind = valueFind + "/" + endpoints[i];

					if (valueFind.equals(path.value())) {

						if (objects != null) {
							if (method.isAnnotationPresent(Produces.class)) {

								Annotation producesAnnotation = method.getAnnotation(Produces.class);
								Produces produces = (Produces) producesAnnotation;

								Object returnObject = method.invoke(newInstance, objects);
								transformProduces(returnObject, produces);
							} else {
								method.invoke(newInstance, objects);
							}
						}

					}
				}

				// There are not Path annotation
			} else if (!method.isAnnotationPresent(Path.class) && endpoints.length <= cont) {
				// methods with return object
				if (method.isAnnotationPresent(Produces.class)) {
					Annotation producesAnnotation = method.getAnnotation(Produces.class);
					Produces produces = (Produces) producesAnnotation;

					// method with object parameters
					if (objects != null) {
						Object returnObject = method.invoke(newInstance, objects);
						transformProduces(returnObject, produces);
					} // method without object parameter
					else {
						Object returnObject = method.invoke(newInstance);
						transformProduces(returnObject, produces);
					}

				} // methods without return object
				else {
					// method with object parameters

					if (objects != null) {
						method.invoke(newInstance, objects);
					} // method without object parameters

					else {
						method.invoke(newInstance);
					}

				}
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

	private void transformProduces(Object obj, Produces prod) throws IOException {

		if (obj != null) {
			for (int i = 0; i < prod.value().length; i++) {
				if (obj.getClass().getName().equals("java.lang.String")
						&& awsRequestApi.getParams().getHeader().get("Content-Type").equals("application/json")
						&& prod.value()[i] == ContentType.APPLICATION_JSON) {
					String stringObject = (String) obj;
					outputStream.write(stringObject.getBytes(Charset.forName("UTF-8")));
					outputStream.close();

				}
			}
		}

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
	
	
	private static String find(String regexpresion, String text) throws SQLException {

		Pattern pattern = Pattern.compile(regexpresion);
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			return matcher.group(0);
		}

		return null;
	}

}

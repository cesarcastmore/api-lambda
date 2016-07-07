package com.framework.api.lambda.client;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.framework.api.lambda.aws.AWSRequestAPI;
import com.framework.api.lambda.aws.AWSRequestAPI.Params;
import com.framework.api.lambda.aws.AWSRequestAPI.Context;
import com.framework.api.lambda.config.Configuration;

public class Request {

	private Configuration config;
	Map<String, String> headers;
	Map<String, String> queries;
	Map<String, String> stageVariables;
	Map<String, String> pathParams;

	String path;

	public Request(Configuration config) {
		this.config = config;
		this.headers = new HashMap<String, String>();
		this.queries = new HashMap<String, String>();
		this.stageVariables = new HashMap<String, String>();
		this.pathParams = new HashMap<String, String>();

	}

	public void header(String key, String value) {
		this.headers.put(key, value);

	}

	public void pathParam(String key, String value) {
		this.pathParams.put(key, value);
	}

	public void path(String path) {
		this.path = path;
	}

	public void query(String key, String value) {
		this.queries.put(key, value);

	}

	public void stageVariable(String key, String value) {
		this.stageVariables.put(key, value);
	}

	private AWSRequestAPI build() {
		AWSRequestAPI awsRequest = new AWSRequestAPI();
		Params params = new Params();
		params.setHeader(headers);
		params.setQuerystring(queries);
		params.setPath(pathParams);
		awsRequest.setParams(params);
		awsRequest.setStageVariables(stageVariables);

		Context contex = new Context();
		contex.setResourcePath(path);
		awsRequest.setContext(contex);

		return awsRequest;
	}

	public Response post(String body) {
		AWSRequestAPI awsRequest = build();
		awsRequest.setBodyJson(body);
		awsRequest.getContext().setHttpMethod("POST");
		OutputStream outputStream = new ByteArrayOutputStream();
		Response response = new Response();

		try {
			config.execute(awsRequest, outputStream);
			response.setBody(outputStream);

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;

	}

	public Response get() {
		AWSRequestAPI awsRequest = build();
		awsRequest.getContext().setHttpMethod("GET");
		OutputStream outputStream = new ByteArrayOutputStream();
		Response response = new Response();

		try {
			config.execute(awsRequest, outputStream);
			response.setBody(outputStream);

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;

	}

	public Response put(String body) {
		AWSRequestAPI awsRequest = build();
		awsRequest.getContext().setHttpMethod("PUT");
		awsRequest.setBodyJson(body);
		OutputStream outputStream = new ByteArrayOutputStream();
		Response response = new Response();

		try {
			config.execute(awsRequest, outputStream);
			response.setBody(outputStream);

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;

	}

	public Response delete() {
		AWSRequestAPI awsRequest = build();
		awsRequest.getContext().setHttpMethod("DELETE");
		OutputStream outputStream = new ByteArrayOutputStream();
		Response response = new Response();

		try {
			config.execute(awsRequest, outputStream);
			response.setBody(outputStream);

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;

	}

}

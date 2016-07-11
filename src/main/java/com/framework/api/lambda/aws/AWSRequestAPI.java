package com.framework.api.lambda.aws;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AWSRequestAPI {

	private String bodyJson;
	private Params params;
	private Context context;
	Map<String, String> stageVariables;

	public static AWSRequestAPI build(String json) throws ParseException {
		JSONParser parser = new JSONParser();
		JSONObject jsonRequest = (JSONObject) parser.parse(json);

		AWSRequestAPI requestAws = new AWSRequestAPI();

		if (jsonRequest.containsKey("body-json")) {
			String bodyJson = (String) jsonRequest.get("body-json");
			requestAws.setBodyJson(bodyJson);

		}
		
	
		

		if (jsonRequest.containsKey("params")) {
			JSONObject paramsJson = (JSONObject) jsonRequest.get("params");
			Params params = Params.build(paramsJson);
			requestAws.setParams(params);
		}
		if (jsonRequest.containsKey("stage-variables")) {

			Map<String, String> mapStageVar= new HashMap<String, String>();
			JSONObject stageVarJson = (JSONObject) jsonRequest.get("stage-variables");

			for (Iterator iterator =  stageVarJson.keySet().iterator(); iterator.hasNext();) {

				String key = (String) iterator.next();
				String value =  stageVarJson.get(key).toString();
				mapStageVar.put(key, value);

			}
			
			requestAws.setStageVariables(mapStageVar);
		}

		JSONObject contexJson = (JSONObject) jsonRequest.get("context");
		Context cont = Context.build(contexJson);
		requestAws.setContext(cont);

		return requestAws;
	}

	public String getBodyJson() {
		return bodyJson;
	}

	public void setBodyJson(String bodyJson) {
		this.bodyJson = bodyJson;
	}

	public Params getParams() {
		return params;
	}

	public void setParams(Params params) {
		this.params = params;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Map<String, String> getStageVariables() {
		return stageVariables;
	}

	public void setStageVariables(Map<String, String> stageVariables) {
		this.stageVariables = stageVariables;
	}
	



	public static class Params {

		public static Params build(JSONObject paramsJson) {

			Params params = new Params();

			if (paramsJson.containsKey("path")) {

				Map<String, String> mapPath = new HashMap<String, String>();
				JSONObject pathJson = (JSONObject) paramsJson.get("path");

				for (Iterator iterator = pathJson.keySet().iterator(); iterator.hasNext();) {

					String key = (String) iterator.next();
					String value = pathJson.get(key).toString();
					mapPath.put(key, value);

				}

				params.setPath(mapPath);

			}

			if (paramsJson.containsKey("querystring")) {
				Map<String, String> mapQueryString = new HashMap<String, String>();
				JSONObject queryStringJson = (JSONObject) paramsJson.get("querystring");

				for (Iterator iterator = queryStringJson.keySet().iterator(); iterator.hasNext();) {

					String key = (String) iterator.next();
					String value = queryStringJson.get(key).toString();
					mapQueryString.put(key, value);

				}

				params.setQuerystring(mapQueryString);

			}

			if (paramsJson.containsKey("header")) {
				Map<String, String> mapHeader = new HashMap<String, String>();
				JSONObject queryHeader = (JSONObject) paramsJson.get("header");

				for (Iterator iterator = queryHeader.keySet().iterator(); iterator.hasNext();) {
					String key = (String) iterator.next();
					String value = queryHeader.get(key).toString();

					queryHeader.put(key, value);

				}

				params.setHeader(queryHeader);

			}

			return params;

		}

		private Map<String, String> path;
		private Map<String, String> querystring;
		private Map<String, String> header;

		public Params() {
			this.path = new HashMap<String, String>();
			this.querystring = new HashMap<String, String>();
			this.header = new HashMap<String, String>();
		}

		public Map<String, String> getPath() {
			return path;
		}

		public void setPath(Map<String, String> path) {
			this.path = path;
		}

		public Map<String, String> getQuerystring() {
			return querystring;
		}

		public void setQuerystring(Map<String, String> querystring) {
			this.querystring = querystring;
		}

		public Map<String, String> getHeader() {
			return header;
		}

		public void setHeader(Map<String, String> header) {
			this.header = header;
		}

	}

	public static class Context {

		public static Context build(JSONObject contextJson) {
			Context context = new Context();

			context.setAccountId((String) contextJson.get("account-id"));
			context.setApiId((String) contextJson.get("api-id"));
			context.setApiKey((String) contextJson.get("api-key"));
			context.setAuthorizerPrincipalId((String) contextJson.get("authorizer-principal-id"));
			context.setCaller((String) contextJson.get("caller"));
			context.setCognitoAuthenticationType((String) contextJson.get("caller"));
			context.setCognitoAuthenticationProvider((String) contextJson.get("cognito-authentication-provider"));
			context.setCognitoIdentityPoolId((String) contextJson.get("cognito-authentication-provider"));
			context.setHttpMethod((String) contextJson.get("http-method"));
			context.setRequestId((String) contextJson.get("request-id"));
			context.setCognitoIdentityPoolId((String) contextJson.get("cognito-identity-pool-id"));
			context.setResourceId((String) contextJson.get("resource-id"));
			context.setResourcePath((String) contextJson.get("resource-path"));
			context.setSourceIp((String) contextJson.get("source-ip"));
			context.setStage((String) contextJson.get("stage"));
			context.setUser((String) contextJson.get("user"));
			context.setUserAgent((String) contextJson.get("user-agent"));
			context.setUserArn((String) contextJson.get("user-arn"));

			return context;
		}

		private String accountId;
		private String apiId;
		private String apiKey;
		private String authorizerPrincipalId;
		private String caller;
		private String cognitoAuthenticationProvider;
		private String cognitoAuthenticationType;
		private String cognitoIdentityId;
		private String cognitoIdentityPoolId;
		private String httpMethod;
		private String stage;
		private String sourceIp;
		private String user;
		private String userAgent;
		private String userArn;
		private String requestId;
		private String resourceId;
		private String resourcePath;

		public String getAccountId() {
			return accountId;
		}

		public void setAccountId(String accountId) {
			this.accountId = accountId;
		}

		public String getApiId() {
			return apiId;
		}

		public void setApiId(String apiId) {
			this.apiId = apiId;
		}

		public String getApiKey() {
			return apiKey;
		}

		public void setApiKey(String apiKey) {
			this.apiKey = apiKey;
		}

		public String getAuthorizerPrincipalId() {
			return authorizerPrincipalId;
		}

		public void setAuthorizerPrincipalId(String authorizerPrincipalId) {
			this.authorizerPrincipalId = authorizerPrincipalId;
		}

		public String getCaller() {
			return caller;
		}

		public void setCaller(String caller) {
			this.caller = caller;
		}

		public String getCognitoAuthenticationProvider() {
			return cognitoAuthenticationProvider;
		}

		public void setCognitoAuthenticationProvider(String cognitoAuthenticationProvider) {
			this.cognitoAuthenticationProvider = cognitoAuthenticationProvider;
		}

		public String getCognitoAuthenticationType() {
			return cognitoAuthenticationType;
		}

		public void setCognitoAuthenticationType(String cognitoAuthenticationType) {
			this.cognitoAuthenticationType = cognitoAuthenticationType;
		}

		public String getCognitoIdentityId() {
			return cognitoIdentityId;
		}

		public void setCognitoIdentityId(String cognitoIdentityId) {
			this.cognitoIdentityId = cognitoIdentityId;
		}

		public String getCognitoIdentityPoolId() {
			return cognitoIdentityPoolId;
		}

		public void setCognitoIdentityPoolId(String cognitoIdentityPoolId) {
			this.cognitoIdentityPoolId = cognitoIdentityPoolId;
		}

		public String getHttpMethod() {
			return httpMethod;
		}

		public void setHttpMethod(String httpMethod) {
			this.httpMethod = httpMethod;
		}

		public String getStage() {
			return stage;
		}

		public void setStage(String stage) {
			this.stage = stage;
		}

		public String getSourceIp() {
			return sourceIp;
		}

		public void setSourceIp(String sourceIp) {
			this.sourceIp = sourceIp;
		}

		public String getUser() {
			return user;
		}

		public void setUser(String user) {
			this.user = user;
		}

		public String getUserAgent() {
			return userAgent;
		}

		public void setUserAgent(String userAgent) {
			this.userAgent = userAgent;
		}

		public String getUserArn() {
			return userArn;
		}

		public void setUserArn(String userArn) {
			this.userArn = userArn;
		}

		public String getRequestId() {
			return requestId;
		}

		public void setRequestId(String requestId) {
			this.requestId = requestId;
		}

		public String getResourceId() {
			return resourceId;
		}

		public void setResourceId(String resourceId) {
			this.resourceId = resourceId;
		}

		public String getResourcePath() {
			return resourcePath;
		}

		public void setResourcePath(String resourcePath) {
			this.resourcePath = resourcePath;
		}

	}

}

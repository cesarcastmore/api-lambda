package com.framework.api.lambda.client;

import java.io.OutputStream;

public class Response {
	
	private OutputStream body;
	private int status;
	
	public Response(){
		
	}

	public OutputStream getBody() {
		return body;
	}

	public void setBody(OutputStream body) {
		this.body = body;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	

}

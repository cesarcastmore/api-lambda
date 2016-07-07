package com.framework.api.lambda.test;

import java.io.ByteArrayOutputStream;

import com.framework.api.lambda.client.Request;
import com.framework.api.lambda.client.Response;

public class Test {
	
	
	public static void main(String args[]){
		delete();
		getAll();
		get();
		post();
		put();
	
	}
	
	public static void delete(){
		
		Request request= new Request(new ConfigurationExample());
		request.path("/invoices/{invoiceId}");
		request.pathParam("invoiceId", "1");
		request.query("_orderBy", "created");
		request.stageVariable("database", "db");
		request.header("Content-Type", "application/json");
		Response response=request.delete();
		
	}
	
	
	public static void getAll(){
		
		Request request= new Request(new ConfigurationExample());
		request.path("/invoices");
		request.query("_orderBy", "created");
		request.stageVariable("database", "db");
		request.header("Content-Type", "application/json");
		Response response=request.get();
		
		ByteArrayOutputStream sos = (ByteArrayOutputStream) response.getBody();
		String value = sos.toString();
		System.out.println(value);
	}
	
	
	public static void get(){
		
		Request request= new Request(new ConfigurationExample());
		request.path("/invoices/{invoiceId}");
		request.pathParam("invoiceId", "1");
		request.stageVariable("database", "db");
		request.header("Content-Type", "application/json");
		Response response=request.get();
		
		ByteArrayOutputStream sos = (ByteArrayOutputStream) response.getBody();
		String value = sos.toString();
		System.out.println(value);
	}
	
	public static void post(){
		
		Request request= new Request(new ConfigurationExample());
		request.path("/invoices");
		request.pathParam("invoiceId", "1");
		request.stageVariable("database", "db");
		request.header("Content-Type", "application/json");
		Response response=request.post("\"name\": \"cesar\"");
		
		ByteArrayOutputStream sos = (ByteArrayOutputStream) response.getBody();
		String value = sos.toString();
		System.out.println(value);
	}
	
	
	public static void put(){
		
		Request request= new Request(new ConfigurationExample());
		request.path("/invoices/{invoiceId}");
		request.pathParam("invoiceId", "1");
		request.stageVariable("database", "db");
		request.header("Content-Type", "application/json");
		Response response=request.put("{\"name\": \"cesar\"}");
		
		ByteArrayOutputStream sos = (ByteArrayOutputStream) response.getBody();
		String value = sos.toString();
		System.out.println(value);
	}

}

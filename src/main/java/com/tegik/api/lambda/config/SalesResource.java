package com.tegik.api.lambda.config;

import com.tegik.api.lambda.annotations.DELETE;
import com.tegik.api.lambda.annotations.GET;
import com.tegik.api.lambda.annotations.POST;
import com.tegik.api.lambda.annotations.PUT;
import com.tegik.api.lambda.annotations.Path;
import com.tegik.api.lambda.annotations.Query;


@Path("sales")
public class SalesResource {
	
	@GET
	public void get(@Query("_orderBy") String _orderBy){
		System.out.println("sales GET: "+ _orderBy );
		
	}
	
	@POST
	public void post(@Query("_orderBy") String _orderBy){
		System.out.println("sales POST: "+ _orderBy );
		
	}
	
	@PUT
	public void put(@Query("_orderBy") String _orderBy){
		System.out.println("sales PUT: "+ _orderBy );
		
	}
	
	@DELETE
	public void delete(@Query("_orderBy") String _orderBy){
		System.out.println("sales DELETE: "+ _orderBy );
		
	}

}

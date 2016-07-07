package com.framework.api.lambda.test;

import com.framework.api.lambda.annotations.DELETE;
import com.framework.api.lambda.annotations.GET;
import com.framework.api.lambda.annotations.POST;
import com.framework.api.lambda.annotations.PUT;
import com.framework.api.lambda.annotations.Path;
import com.framework.api.lambda.annotations.QueryParam;


@Path("sales")
public class SalesResource {
	
	@GET
	public void get(@QueryParam("_orderBy") String _orderBy){
		System.out.println("sales GET: "+ _orderBy );
		
	}
	
	@POST
	public void post(@QueryParam("_orderBy") String _orderBy){
		System.out.println("sales POST: "+ _orderBy );
		
	}
	
	@PUT
	public void put(@QueryParam("_orderBy") String _orderBy){
		System.out.println("sales PUT: "+ _orderBy );
		
	}
	
	@DELETE
	public void delete(@QueryParam("_orderBy") String _orderBy){
		System.out.println("sales DELETE: "+ _orderBy );
		
	}

}

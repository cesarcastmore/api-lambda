package com.framework.api.lambda.config;

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

@Path("invoices")
public class InvoiceResource {
	
	@StageVariable("apellido")
	public String apellido;
	

	@GET
	@Produces(ContentType.APPLICATION_JSON)
	public String getall(@QueryParam("_orderBy") String _orderBy) {
		System.out.println("invoices GET ALL: " + _orderBy);
		return null;

	}

	@GET
	@Path("{invoiceId}")
	@Produces(ContentType.APPLICATION_JSON)
	public String get(@PathParam("invoiceId") String invoiceId, String json) {
		System.out.println("invoices GET ONE: " + invoiceId);
		
		return null;

	}

	@POST
	@Consumes(ContentType.APPLICATION_JSON)
	@Produces(ContentType.APPLICATION_JSON)
	public String post(String json) {
		System.out.println("invoices POST: " + json + " apellido "+ apellido);
		return "{\"name\": \"Cesar\"}";

	}

	@PUT
	@Consumes(ContentType.APPLICATION_JSON)
	@Path("{invoiceId}")
	@Produces(ContentType.APPLICATION_JSON)
	public String put(@PathParam("invoiceId") String invoiceId, String json) {
		System.out.println("invoices PUT: " + "json: "+  json+  " invoice Id"+ invoiceId);
		return "{\"name\": \"Cesar\"}";


	}

	@DELETE
	@Path("{invoiceId}")
	public void delete(@PathParam("invoiceId") String invoiceId) {
		System.out.println("invoices DELETE: invoiceId"+ invoiceId);

	}

}

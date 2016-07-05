package com.tegik.api.lambda.config;

import com.tegik.api.lambda.annotations.Consumes;
import com.tegik.api.lambda.annotations.ContentType;
import com.tegik.api.lambda.annotations.DELETE;
import com.tegik.api.lambda.annotations.GET;
import com.tegik.api.lambda.annotations.POST;
import com.tegik.api.lambda.annotations.PUT;
import com.tegik.api.lambda.annotations.Path;
import com.tegik.api.lambda.annotations.PathParam;
import com.tegik.api.lambda.annotations.QueryParam;

@Path("invoices")
public class InvoiceResource {

	@GET
	public void getall(@QueryParam("_orderBy") String _orderBy) {
		System.out.println("invoices GET ALL: " + _orderBy);

	}

	@GET
	@Path("{invoiceId}")
	public void get(@PathParam("invoiceId") String invoiceId, String json) {
		System.out.println("invoices GET ONE: " + invoiceId);

	}

	@POST
	@Consumes(ContentType.APPLICATION_JSON)
	public void post(String json) {
		System.out.println("invoices POST: " + json );

	}

	@PUT
	@Consumes(ContentType.APPLICATION_JSON)
	@Path("{invoiceId}")
	public void put(@PathParam("invoiceId") String invoiceId, String json) {
		System.out.println("invoices PUT: " + "json: "+  json+  " invoice Id"+ invoiceId);

	}

	@DELETE
	@Path("{invoiceId}")
	public void delete(@PathParam("invoiceId") String invoiceId) {
		System.out.println("invoices DELETE: invoiceId"+ invoiceId);

	}

}

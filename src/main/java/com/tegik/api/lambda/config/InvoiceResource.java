package com.tegik.api.lambda.config;

import com.tegik.api.lambda.annotations.DELETE;
import com.tegik.api.lambda.annotations.GET;
import com.tegik.api.lambda.annotations.POST;
import com.tegik.api.lambda.annotations.PUT;
import com.tegik.api.lambda.annotations.Path;
import com.tegik.api.lambda.annotations.PathParam;
import com.tegik.api.lambda.annotations.Query;

@Path("invoices")
public class InvoiceResource {

	@GET
	public void getall(@Query("_orderBy") String _orderBy) {
		System.out.println("invoices GET ALL: " + _orderBy);

	}

	@GET
	@Path("{invoiceId}")
	public void get(@PathParam("invoiceId") String invoiceId) {
		System.out.println("invoices GET ONE: " + invoiceId);

	}

	@POST
	public void post(@Query("_orderBy") String _orderBy) {
		System.out.println("invoices POST: " + _orderBy);

	}

	@PUT
	public void put(@Query("_orderBy") String _orderBy) {
		System.out.println("invoices PUT: " + _orderBy);

	}

	@DELETE
	public void delete(@Query("_orderBy") String _orderBy) {
		System.out.println("invoices DELETE: " + _orderBy);

	}

}

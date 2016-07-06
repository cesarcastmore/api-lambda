package com.framework.api.lambda.config;

import java.util.HashSet;
import java.util.Set;

public class ConfigurationExample extends Configuration{

	public Set<Class<?>> getClasses() {
		// TODO Auto-generated method stub
		Set<Class<?>> resources = new HashSet<Class<?>>();
		resources.add(SalesResource.class);
		resources.add(InvoiceResource.class);

		return resources;
	}
	
	

}

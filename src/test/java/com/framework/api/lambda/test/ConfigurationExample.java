package com.framework.api.lambda.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import com.framework.api.lambda.config.Configuration;

public class ConfigurationExample extends Configuration{

	public Set<Class<?>> getClasses() {
		// TODO Auto-generated method stub
		Set<Class<?>> resources = new HashSet<Class<?>>();
		resources.add(SalesResource.class);
		resources.add(InvoiceResource.class);

		return resources;
	}
	

}

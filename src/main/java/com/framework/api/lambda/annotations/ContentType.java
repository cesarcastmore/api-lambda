package com.framework.api.lambda.annotations;


public enum ContentType {
	
	APPLICATION_JSON, APPLICATION_XML;
	
	
    public String value() {
        return name();
    }

    public static ContentType fromValue(String v) {
        return valueOf(v);
    }

}

package org.smart4j.framework.bean;

//封装表单的单个域的一个bean
public class FormParam {

	private String fieldName;
	
	private Object fieldValue;

	public FormParam(String fieldName, Object fieldValue) {
		super();
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public String getFieldName() {
		return fieldName;
	}

	public Object getFieldValue() {
		return fieldValue;
	}
	
	
}

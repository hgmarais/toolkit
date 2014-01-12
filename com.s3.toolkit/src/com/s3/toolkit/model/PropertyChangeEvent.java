package com.s3.toolkit.model;

public class PropertyChangeEvent implements ModelEvent {

	private Model model;
	
	private String propertyName;
	
	private Object value;

	public PropertyChangeEvent(Model model, String propertyName, Object value) {
		this.model = model;
		this.propertyName = propertyName;
		this.value = value;
	}
	
	public Model getModel() {
		return model;
	}
	
	public String getPropertyName() {
		return propertyName;
	}
	
	public Object getValue() {
		return value;
	}

}

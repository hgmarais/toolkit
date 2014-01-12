package com.s3.toolkit.model;

public class AbstractModelEvent implements ModelEvent {
	
	private Model model;
	
	public AbstractModelEvent(Model model) {
		this.model = model;
	}
	
	@Override
	public Model getModel() {
		return model;
	}

}

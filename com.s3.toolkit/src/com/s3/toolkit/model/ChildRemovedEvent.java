package com.s3.toolkit.model;

public class ChildRemovedEvent extends AbstractModelEvent {
	
	private Model child;

	public ChildRemovedEvent(Model parent, Model child) {
		super(parent);
		this.child = child;
	}
	
	public Model getParent() {
		return getModel();
	}
	
	public Model getChild() {
		return child;
	}

}

package com.s3.toolkit.model;

public interface Model {

	public abstract Model getParent();

	public abstract void addListener(ModelListener listener);

	public abstract void removeListener(ModelListener listener);

}
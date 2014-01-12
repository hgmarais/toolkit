package com.s3.toolkit.cs.model;

import org.dom4j.Element;

public class DatabaseConnectionModel extends ConnectionModel {
	
	public static final String DATABASE_ELEMENT = "Database";
	
	public DatabaseConnectionModel(ConnectionsModel connections) {
		super(connections);
	}

	@Override
	protected void exportElement(Element element) {
	}

	@Override
	public String getElementName() {
		return DATABASE_ELEMENT;
	}
	
}

package com.s3.toolkit.model;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public abstract class AbstractXMLModel extends AbstractModel implements XMLModel {
	
	public AbstractXMLModel() {
	}
	
	public AbstractXMLModel(AbstractModel parent) {
		super(parent);
	}

	@Override
	public Element exportElement() {
		return DocumentHelper.createElement(getElementName());
	}

}

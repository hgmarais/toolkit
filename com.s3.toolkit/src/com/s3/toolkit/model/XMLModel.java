package com.s3.toolkit.model;

import org.dom4j.Element;

public interface XMLModel extends Model {
	
	String getElementName();
	
	Element exportElement();
	
	void importElement(Element element);

}

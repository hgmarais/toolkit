package com.s3.toolkit.gui.model;

import org.dom4j.Element;

import com.s3.toolkit.model.AbstractModel;
import com.s3.toolkit.model.AbstractXMLModel;

public class PropertyModel extends AbstractXMLModel {
	
	public static final String ELEMENT_NAME = "Property";
	
	public PropertyModel(AbstractModel parent) {
		super(parent);
	}

	@Override
	public String getElementName() {
		return ELEMENT_NAME;
	}

	@Override
	public void importElement(Element element) {
	}

	public static PropertyModel importElement(GUIDeviceModel guiDevice, Element propertyElement) {
		PropertyModel result = new PropertyModel(guiDevice);
		result.importElement(propertyElement);
		return result;
	}

}

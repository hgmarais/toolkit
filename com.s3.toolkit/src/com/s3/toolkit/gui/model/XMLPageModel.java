package com.s3.toolkit.gui.model;

import org.dom4j.Element;

import com.s3.toolkit.model.AbstractXMLModel;
import com.s3.toolkit.model.AbstractModel;

public class XMLPageModel extends AbstractXMLModel {
	
	public static final String ELEMENT_NAME = "XMLPage";
	
	public static final String NAME_ATTRIBUTE = "name";
	
	private String name = "";
	
	public XMLPageModel(AbstractModel parent) {
		super(parent);
	}
	
	public void setName(String name) {
		this.name = name;
		firePropertyChange(NAME_ATTRIBUTE, name);
	}
	
	public String getName() {
		return name;
	}

	@Override
	public String getElementName() {
		return ELEMENT_NAME;
	}

	@Override
	public Element exportElement() {
		Element result = super.exportElement();
		
		result.addAttribute(NAME_ATTRIBUTE, name);
		
		return result;
	}

	@Override
	public void importElement(Element element) {
		name = element.attributeValue(NAME_ATTRIBUTE, "");
	}
	
	public static XMLPageModel importElement(GUIDeviceModel guiDevice, Element xmlPageElement) {
		XMLPageModel result = new XMLPageModel(guiDevice);
		result.importElement(xmlPageElement);
		return result;
	}

}

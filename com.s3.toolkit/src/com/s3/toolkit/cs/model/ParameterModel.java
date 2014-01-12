package com.s3.toolkit.cs.model;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.s3.toolkit.model.AbstractModel;

public class ParameterModel extends AbstractModel {
	
	public static final String PARAMETER_ELEMENT = "Parameter";
	
	public static final String PREFIX_ATTRIBUTE = "prefix";
	
	public static final String TAG_NAME_ATTRIBUTE = "tagName";
	
	public static final String REGISTER_ATTRIBUTE = "register";
	
	private String prefix = "";
	
	private String tagName = "";
	
	private String register = "";
	
	public ParameterModel(PeripheralModel peripheral) {
		super(peripheral);
	}
	
	public ParameterModel(BlockModel block) {
		super(block);
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
		firePropertyChange(PREFIX_ATTRIBUTE, prefix);
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public void setTagName(String tagName) {
		this.tagName = tagName;
		firePropertyChange(TAG_NAME_ATTRIBUTE, tagName);
	}

	public String getTagName() {
		return tagName;
	}
	
	public void setRegister(String register) {
		this.register = register;
		firePropertyChange(REGISTER_ATTRIBUTE, register);
	}
	
	public String getRegister() {
		return register;
	}

	public void loadFromElement(Element parameterElement) {
		prefix = parameterElement.attributeValue(PREFIX_ATTRIBUTE, "");
		tagName = parameterElement.attributeValue(TAG_NAME_ATTRIBUTE, "");
		register = parameterElement.attributeValue(REGISTER_ATTRIBUTE, "");
	}
	
	public Element exportElement() {
		Element result = DocumentHelper.createElement(PARAMETER_ELEMENT);
		
		result.addAttribute(PREFIX_ATTRIBUTE, prefix);
		result.addAttribute(TAG_NAME_ATTRIBUTE, tagName);
		result.addAttribute(REGISTER_ATTRIBUTE, register);
		
		return result;
	}
	
	public static ParameterModel load(PeripheralModel peripheral, Element parameterElement) {
		ParameterModel result = new ParameterModel(peripheral);
		result.loadFromElement(parameterElement);
		return result;
	}

	public static ParameterModel load(BlockModel block, Element parameterElement) {
		ParameterModel result = new ParameterModel(block);
		result.loadFromElement(parameterElement);
		return result;
	}
	
}

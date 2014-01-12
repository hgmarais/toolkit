package com.s3.toolkit.cs.model;

import java.util.LinkedList;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.s3.toolkit.model.AbstractModel;

public class BlockModel extends AbstractModel {
	
	public static final String BLOCK_ELEMENT = "Block";

	public static final String BLOCK_TYPE_ATTRIBUTE = "blockType";
	
	public static final String BLOCK_NAME_ATTRIBUTE = "blockName";
	
	private String blockType = "";
	
	private String blockName = "";
	
	private List<ParameterModel> parameters;

	public BlockModel(PeripheralModel peripheral) {
		super(peripheral);
		parameters = new LinkedList<ParameterModel>();
	}

	public void setBlockType(String blockType) {
		this.blockType = blockType;
		firePropertyChange(BLOCK_TYPE_ATTRIBUTE, blockType);
	}
	
	public String getBlockType() {
		return blockType;
	}
	
	public void setBlockName(String blockName) {
		this.blockName = blockName;
		firePropertyChange(BLOCK_NAME_ATTRIBUTE, blockName);
	}
	
	public String getBlockName() {
		return blockName;
	}
	
	public List<ParameterModel> getParameters() {
		return parameters;
	}
	
	public Element exportElement() {
		Element result = DocumentHelper.createElement(BLOCK_ELEMENT);
		
		result.addAttribute(BLOCK_TYPE_ATTRIBUTE, blockType);
		result.addAttribute(BLOCK_NAME_ATTRIBUTE, blockName);
		
		for (ParameterModel parameter : parameters) {
			result.add(parameter.exportElement());
		}
		
		return result;
	}
	
	private void loadFromElement(Element blockElement) {
		blockType = blockElement.attributeValue(BLOCK_TYPE_ATTRIBUTE, "");
		blockName = blockElement.attributeValue(BLOCK_NAME_ATTRIBUTE, "");
		
		List<Element> parameterElements = blockElement.elements(ParameterModel.PARAMETER_ELEMENT);
		
		for (Element parameterElement : parameterElements) {
			parameters.add(ParameterModel.load(this, parameterElement));
		}
	}
	
	public static BlockModel load(PeripheralModel peripheral, Element blockElement) {
		BlockModel result = new BlockModel(peripheral);
		result.loadFromElement(blockElement);
		return result;
	}
	
}

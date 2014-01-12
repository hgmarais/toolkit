package com.s3.toolkit.gui.model;

import java.util.LinkedList;
import java.util.List;

import org.dom4j.Element;

import com.s3.toolkit.model.AbstractModel;
import com.s3.toolkit.model.AbstractXMLModel;

public class PageModel extends AbstractXMLModel {
	
	public static final String ELEMENT_NAME = "Page";
	
	public static final String NAME_ATTRIBUTE = "name";
	
	private String name = "";
	
	private List<GroupModel> groups = new LinkedList<GroupModel>();
	
	public PageModel(AbstractModel parent) {
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
	
	public List<GroupModel> getGroups() {
		return groups;
	}
	
	@Override
	public Element exportElement() {
		Element result = super.exportElement();
		
		result.addAttribute(NAME_ATTRIBUTE, name);
		
		for (GroupModel group : groups) {
			result.add(group.exportElement());
		}
		
		return result;
	}

	@Override
	public void importElement(Element element) {
		name = element.attributeValue(NAME_ATTRIBUTE, "");
		groups = GroupModel.importFromParent(this, element);
	}

	public static PageModel importElement(GUIDeviceModel guiDevice, Element pageElement) {
		PageModel result = new PageModel(guiDevice);
		result.importElement(pageElement);
		return result;
	}

}

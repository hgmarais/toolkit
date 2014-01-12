package com.s3.toolkit.gui.model;

import java.util.LinkedList;
import java.util.List;

import org.dom4j.Element;

import com.s3.toolkit.model.AbstractXMLModel;

public class GroupModel extends AbstractXMLModel {
	
	public static final String ELEMENT_NAME = "Group";
	
	public static final String NAME_ATTRIBUTE = "name";
	
	public static final String TITLE_ATTRIBUTE = "title";
	
	public static final String LOAD_LOGIC_ATTRIBUTE = "loadLogic";
	
	private String name = "";
	
	private String title = "";
	
	private String loadLogic = "";
	
	public GroupModel(PageModel page) {
		super(page);
	}

	public void setName(String name) {
		this.name = name;
		firePropertyChange(NAME_ATTRIBUTE, name);
	}
	
	public String getName() {
		return name;
	}
	
	public void setTitle(String title) {
		this.title = title;
		firePropertyChange(TITLE_ATTRIBUTE, title);
	}
	
	public String getTitle() {
		return title;
	}

	public void setLoadLogic(String loadLogic) {
		this.loadLogic = loadLogic;
		firePropertyChange(LOAD_LOGIC_ATTRIBUTE, loadLogic);
	}
	
	public String getLoadLogic() {
		return loadLogic;
	}
	
	@Override
	public String getElementName() {
		return ELEMENT_NAME;
	}
	
	@Override
	public Element exportElement() {
		Element result = super.exportElement();
		
		result.addAttribute(NAME_ATTRIBUTE, name);
		result.addAttribute(TITLE_ATTRIBUTE, title);
		result.addAttribute(LOAD_LOGIC_ATTRIBUTE, loadLogic);
		
		return result;
	}

	@Override
	public void importElement(Element element) {
		name = element.attributeValue(NAME_ATTRIBUTE, "");
		title = element.attributeValue(TITLE_ATTRIBUTE, "");
		loadLogic = element.attributeValue(LOAD_LOGIC_ATTRIBUTE, "");
	}

	public static List<GroupModel> importFromParent(PageModel page, Element pageElement) {
			List<GroupModel> result = new LinkedList<GroupModel>();
			List<Element> groupElements = pageElement.elements(ELEMENT_NAME);
			
			for (Element groupElement : groupElements) {
				GroupModel group = new GroupModel(page);
				group.importElement(groupElement);
				result.add(group);
			}
			
			return result;
		}

}

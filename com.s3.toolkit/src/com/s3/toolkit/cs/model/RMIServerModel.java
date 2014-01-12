package com.s3.toolkit.cs.model;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.s3.toolkit.model.AbstractModel;


public class RMIServerModel extends AbstractModel {
	
	public static final String RMI_SERVER_ELEMENT = "RMIServer";
	
	public static final String OBJ_BIND_NAME_PROPERTY = "objBindName";
	
	private boolean enabled = true;
	
	private String objBindName = "";
	
	public RMIServerModel(ServerModel parent) {
		super(parent);
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public void setObjBindName(String objBindName) {
		this.objBindName = objBindName;
		firePropertyChange(OBJ_BIND_NAME_PROPERTY, objBindName);
	}
	
	public String getObjBindName() {
		return objBindName;
	}

	public void loadFromParent(Element parent) {
		Element rmiServerElement = parent.element(RMI_SERVER_ELEMENT);
		
		if (rmiServerElement == null) {
//			enabled = false;
			return;
		}
		
		enabled = true;
		objBindName = rmiServerElement.attributeValue(OBJ_BIND_NAME_PROPERTY, "");
	}

	public Element exportElement() {
		Element result = DocumentHelper.createElement(RMI_SERVER_ELEMENT);
		result.addAttribute(OBJ_BIND_NAME_PROPERTY, objBindName);
		return result;
	}

}

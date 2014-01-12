package com.s3.toolkit.cs.model;

import org.dom4j.Element;

public class RemoteRMIConnectionModel extends ConnectionModel {
	
	public static final String REMOTE_RMI_ELEMENT = "RemoteRMI";
	
	public static final String HOST_NAME_ATTRIBUTE = "hostName";
	
	public static final String OBJ_NAME_ATTRIBUTE = "objName";
	
	private String hostName = "";
	
	private String objName = "";
	
	public RemoteRMIConnectionModel(ConnectionsModel connections) {
		super(connections);
	}
	
	public void setHostName(String hostName) {
		this.hostName = hostName;
		firePropertyChange(HOST_NAME_ATTRIBUTE, hostName);
	}
	
	public String getHostName() {
		return hostName;
	}
	
	public void setObjName(String objName) {
		this.objName = objName;
		firePropertyChange(OBJ_NAME_ATTRIBUTE, objName);
	}
	
	public String getObjName() {
		return objName;
	}

	@Override
	protected void exportElement(Element element) {
		element.addAttribute(HOST_NAME_ATTRIBUTE, hostName);
		element.addAttribute(OBJ_NAME_ATTRIBUTE, objName);
	}
	
	@Override
	public void loadFromElement(Element connectionElement) {
		super.loadFromElement(connectionElement);
		
		hostName = connectionElement.attributeValue(HOST_NAME_ATTRIBUTE, "");
		objName = connectionElement.attributeValue(OBJ_NAME_ATTRIBUTE, "");
	}

	@Override
	public String getElementName() {
		return REMOTE_RMI_ELEMENT;
	}

}

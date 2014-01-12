package com.s3.toolkit.cs.model;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.s3.toolkit.model.AbstractModel;

public class CSGUIServerModel extends AbstractModel {
	
	public static final String GUI_SERVER_ELEMENT = "GUIServer";
	
	public static final String SERVER_ID_PROPERTY = "serverId";
	
	public static final String PORT_NUMBER_PROPERTY = "portNumber";
	
	public static final String CONFIG_XML_PROPERTY = "configXML";
	
	private boolean enabled = true;
	
	private String serverId = "";
	
	private String portNumber = "0";
	
	private String configXML = "";
	
	public CSGUIServerModel(ServerModel parent) {
		super(parent);
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
		firePropertyChange(SERVER_ID_PROPERTY, serverId);
	}

	public String getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
		firePropertyChange(PORT_NUMBER_PROPERTY, portNumber);
	}

	public String getConfigXML() {
		return configXML;
	}

	public void setConfigXML(String configXML) {
		this.configXML = configXML;
		firePropertyChange(CONFIG_XML_PROPERTY, configXML);
	}
	
	public void loadFromParent(Element parent) {
		Element guiServerElement = parent.element(GUI_SERVER_ELEMENT);
		
		if (guiServerElement == null) {
//			enabled = false;
			return;
		}
		
		enabled = true;
		serverId = guiServerElement.attributeValue(SERVER_ID_PROPERTY, "");
		portNumber = guiServerElement.attributeValue(PORT_NUMBER_PROPERTY, "");
		configXML = guiServerElement.attributeValue(CONFIG_XML_PROPERTY, "");
	}

	public Element exportElement() {
		Element result = DocumentHelper.createElement(GUI_SERVER_ELEMENT);
		
		result.addAttribute(SERVER_ID_PROPERTY, serverId);
		result.addAttribute(PORT_NUMBER_PROPERTY, portNumber);
		result.addAttribute(CONFIG_XML_PROPERTY, configXML);
		
		return result;
	}

}

package com.s3.toolkit.cs.model;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.s3.toolkit.model.AbstractModel;

public abstract class ConnectionModel extends AbstractModel {
	
	public static final String CONN_NAME_ATTRIBUTE = "connName";
	
	private String connName = "";
	
	public ConnectionModel(ConnectionsModel connections) {
		super(connections);
	}
	
	public String getConnName() {
		return connName;
	}
	
	public void setConnName(String connName) {
		this.connName = connName;
		firePropertyChange(CONN_NAME_ATTRIBUTE, connName);
	}
	
	public Element exportElement() {
		Element result = DocumentHelper.createElement(getElementName());
		
		result.addAttribute(CONN_NAME_ATTRIBUTE, connName);
		exportElement(result);
		
		return result;
	}
	
	public void loadFromElement(Element connectionElement) {
		connName = connectionElement.attributeValue(CONN_NAME_ATTRIBUTE, "");
	}
	
	protected abstract void exportElement(Element element);
	
	public abstract String getElementName();

	public static ConnectionModel load(ConnectionsModel connections, Element connectionElement) {
		ConnectionModel result = null;
		String name = connectionElement.getName();
		
		if (DatabaseConnectionModel.DATABASE_ELEMENT.equals(name)) {
			result = new DatabaseConnectionModel(connections);
		} else if (RemoteRMIConnectionModel.REMOTE_RMI_ELEMENT.equals(name)) {
			result = new RemoteRMIConnectionModel(connections);
		}
		
		if (result != null) {
			result.loadFromElement(connectionElement);
		}
		
		return result;
	}

}

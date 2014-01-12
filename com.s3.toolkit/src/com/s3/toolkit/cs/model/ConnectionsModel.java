package com.s3.toolkit.cs.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.s3.toolkit.model.AbstractModel;

public class ConnectionsModel extends AbstractModel {
	
	public static final String CONNECTIONS_ELEMENT = "Connection";
	
	private List<ConnectionModel> connections;
	
	public ConnectionsModel(CSModel parent) {
		super(parent);
		connections = new LinkedList<ConnectionModel>();
	}
	
	public List<ConnectionModel> getConnections() {
		return connections;
	}
	
	public int getConnectionCount() {
		return connections.size();
	}
	
	public ConnectionModel getConnection(String connectionName) {
		for (ConnectionModel connection : connections) {
			if (connectionName.equals(connection.getConnName())) {
				return connection;
			}
		}
		
		return null;
	}

	public Element exportElement() {
		Element result = DocumentHelper.createElement(CONNECTIONS_ELEMENT);
		
		for (ConnectionModel connection : connections) {
			result.add(connection.exportElement());
		}
		
		return result;
	}
	
	public void loadFromParent(Element csElement) {
		Element connectionsElement = csElement.element(CONNECTIONS_ELEMENT);
		
		if (connectionsElement == null) {
			return;
		}
		
		Iterator iterator = connectionsElement.elementIterator();
		
		while (iterator.hasNext()) {
			Element connectionElement = (Element) iterator.next();
			ConnectionModel connection = ConnectionModel.load(this, connectionElement);
			
			if (connection != null) {
				connections.add(connection);
			}
		}
	}

}

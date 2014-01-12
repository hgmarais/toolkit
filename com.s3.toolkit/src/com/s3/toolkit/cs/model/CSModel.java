package com.s3.toolkit.cs.model;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.s3.toolkit.model.AbstractXMLModel;

public class CSModel extends AbstractXMLModel {
	
	public static final String CONNECTIVITY_SERVER_ELEMENT = "ConnectivityServer";
	
	private ServerModel server;
	
	private ConnectionsModel connections;
	
	private List<DeviceModel> devices;
	
	public CSModel() {
		server = new ServerModel(this);
		connections = new ConnectionsModel(this);
		devices = new LinkedList<DeviceModel>();
	}
	
	public ServerModel getServer() {
		return server;
	}
	
	public ConnectionsModel getConnections() {
		return connections;
	}
	
	public List<DeviceModel> getDevices() {
		return devices;
	}

	@Override
	public String getElementName() {
		return CONNECTIVITY_SERVER_ELEMENT;
	}

	@Override
	public void importElement(Element csElement) {
		server.loadFromParent(csElement);
		connections.loadFromParent(csElement);
		devices = DeviceModel.loadFromParent(this, csElement);
	}
	
	@Override
	public Element exportElement() {
		Element result = super.exportElement();

		result.add(server.exportElement());
		result.add(connections.exportElement());

		for (DeviceModel device : devices) {
			result.add(device.exportElement());
		}

		return result;
	}
	
//	public Document exportDocument() {
//		Document document = DocumentHelper.createDocument();
//		document.add(exportElement());
//		return document;
//	}
//	
//	public Element exportElement() {
//		Element result = DocumentHelper.createElement(CONNECTIVITY_SERVER_ELEMENT);
//		
//		result.add(server.exportElement());
//		result.add(connections.exportElement());
//		
//		for (DeviceModel device : devices) {
//			result.add(device.exportElement());
//		}
//		
//		return result;
//	}
//
//	public byte[] exportBytes() throws IOException {
//		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//		OutputFormat format = OutputFormat.createPrettyPrint();
//		XMLWriter writer = new XMLWriter(outputStream, format);
//		
//		writer.write(exportDocument());
//		writer.flush();
//		
//		return outputStream.toByteArray();
//	}
//	
//	public String exportXML() throws IOException {
//		return new String(exportBytes());
//	}

}

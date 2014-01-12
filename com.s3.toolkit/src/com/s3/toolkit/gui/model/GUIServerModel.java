package com.s3.toolkit.gui.model;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.s3.toolkit.cs.model.CSModel;
import com.s3.toolkit.model.AbstractXMLModel;

public class GUIServerModel extends AbstractXMLModel {
	
	public static final String ELEMENT_NAME = "GUIServer";
	
	private List<GUIDeviceModel> devices = new LinkedList<GUIDeviceModel>();
	
	public List<GUIDeviceModel> getDevices() {
		return devices;
	}
	
	@Override
	public String getElementName() {
		return ELEMENT_NAME;
	}

	@Override
	public void importElement(Element guiServerElement) {
		devices = GUIDeviceModel.importFromParent(this, guiServerElement);
	}
	
	@Override
	public Element exportElement() {
		Element result = super.exportElement();
		
		for (GUIDeviceModel device : devices) {
			result.add(device.exportElement());
		}
		
		return result;
	}
	
	public static GUIServerModel load(InputStream inputStream) throws DocumentException {
		SAXReader reader = new SAXReader(false);
		Document document = reader.read(inputStream);
		Element root = document.getRootElement();
		
		if (!ELEMENT_NAME.equals(root.getName())) {
			throw new DocumentException("GUIServer element not found.");
		}
		
		GUIServerModel result = new GUIServerModel();
		result.importElement(root);
		return result;
	}

}

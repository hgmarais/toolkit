package com.s3.toolkit.gui.model;

import java.util.LinkedList;
import java.util.List;

import org.dom4j.Element;

import com.s3.toolkit.model.AbstractXMLModel;
import com.s3.toolkit.model.XMLModel;

public class GUIDeviceModel extends AbstractXMLModel {
	
	public static final String ELEMENT_NAME = "Device";
	
	public static final String DEVICE_NAME_ATTRIBUTE = "deviceName";
	
	public static final String ALARMS_ENABLED_ATTRIBUTE = "alarmsEnabled";
	
	public static final String LOAD_LOGIC_ATTRIBUTE = "loadLogic";
	
	private String deviceName = "";
	
	private boolean alarmsEnabled = true;
	
	private String loadLogic = "";
	
	private List<XMLModel> children = new LinkedList<XMLModel>();
	
	public GUIDeviceModel(GUIServerModel guiServer) {
		super(guiServer);
	}
	
	@Override
	public GUIServerModel getParent() {
		return (GUIServerModel) super.getParent();
	}
	
	@Override
	public String getElementName() {
		return ELEMENT_NAME;
	}
	
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
		firePropertyChange(DEVICE_NAME_ATTRIBUTE, deviceName);
	}
	
	public String getDeviceName() {
		return deviceName;
	}
	
	public void setAlarmsEnabled(boolean alarmsEnabled) {
		this.alarmsEnabled = alarmsEnabled;
		firePropertyChange(ALARMS_ENABLED_ATTRIBUTE, alarmsEnabled);
	}
	
	public boolean isAlarmsEnabled() {
		return alarmsEnabled;
	}
	
	public void setLoadLogic(String loadLogic) {
		this.loadLogic = loadLogic;
		firePropertyChange(LOAD_LOGIC_ATTRIBUTE, loadLogic);
	}
	
	public String getLoadLogic() {
		return loadLogic;
	}
	
	public List<XMLModel> getChildren() {
		return children;
	}
	
	public void addPage(PageModel page) {
		addChild(page);
	}
	
	public void addProperty(PropertyModel property) {
		addChild(property);
	}
	
	public void addXMLPage(XMLPageModel xmlPage) {
		addChild(xmlPage);
	}
	
	private void addChild(XMLModel child) {
		children.add(child);
	}
	
	@Override
	public Element exportElement() {
		Element result = super.exportElement();
		
		result.addAttribute(DEVICE_NAME_ATTRIBUTE, deviceName);
		result.addAttribute(ALARMS_ENABLED_ATTRIBUTE, Boolean.toString(alarmsEnabled));
		result.addAttribute(LOAD_LOGIC_ATTRIBUTE, loadLogic);
		
		return result;
	}
	
	@Override
	public void importElement(Element element) {
		deviceName = element.attributeValue(DEVICE_NAME_ATTRIBUTE, "");
		alarmsEnabled = "true".equals(element.attributeValue(ALARMS_ENABLED_ATTRIBUTE, "true"));
		loadLogic = element.attributeValue(LOAD_LOGIC_ATTRIBUTE, "");
		
		List<Element> childElements = element.elements();
		
		for (Element childElement : childElements) {
			String name = childElement.getName();
			
			if (PageModel.ELEMENT_NAME.equals(name)) {
				children.add(PageModel.importElement(this, childElement));
			} else if (PropertyModel.ELEMENT_NAME.equals(name)) {
				children.add(PropertyModel.importElement(this, childElement));
			} else if (XMLPageModel.ELEMENT_NAME.equals(name)) {
				children.add(XMLPageModel.importElement(this, childElement));
			}
		}
	}

	public static List<GUIDeviceModel> importFromParent(GUIServerModel guiServer, Element guiServerElement) {
		List<GUIDeviceModel> result = new LinkedList<GUIDeviceModel>();
		List<Element> deviceElements = guiServerElement.elements(ELEMENT_NAME);
		
		for (Element deviceElement : deviceElements) {
			GUIDeviceModel device = new GUIDeviceModel(guiServer);
			device.importElement(deviceElement);
			result.add(device);
		}
		
		return result;
	}

}

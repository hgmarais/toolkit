package com.s3.toolkit.cs.model;

import java.util.LinkedList;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.s3.toolkit.model.AbstractModel;

public class DeviceModel extends AbstractModel {
	
	public static final String DEVICE_ELEMENT = "Device";
	
	public static final String NAME_ATTRIBUTE = "name";
	
	public static final String MACHINE_CLASS_ATTRIBUTE = "machineClass";
	
	public static final String VENDOR_ATTRIBUTE = "vendor";
	
	public static final String MACHINE_MODEL_ATTRIBUTE = "machineModel";
	
	public static final String MACHINE_TYPE_ATTRIBUTE = "machineType";
	
	public static final String WS_PORT_NUMBER_ATTRIBUTE = "wsPortNumber";
	
	public static final String DEFAULT_MACHINE_TYPE = "FT";
	
	public static final String DEFAULT_WS_PORT_NUMBER = "0";
	
	public static final String DEFAULT_VENDOR = "Filtec";
	
	private String name = "";
	
	private String machineClass = "";
	
	private String vendor = DEFAULT_VENDOR;
	
	private String machineModel = DEFAULT_MACHINE_TYPE;
	
	private String machineType = "";
	
	private String wsPortNumber = DEFAULT_WS_PORT_NUMBER;
	
	private List<PeripheralModel> peripherals;
	
//private List<VirtualPeripheralModel> virtualPeripherals;

//private MachineSyncModel machineSync;

//private MachineChangeDetectionModel machineChangeDetection;

//private List<ExpressionModel> expression;
	
	public DeviceModel(CSModel parent) {
		super(parent);
		peripherals = new LinkedList<PeripheralModel>();
	}
	
	@Override
	public CSModel getParent() {
		return (CSModel)super.getParent();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		firePropertyChange(NAME_ATTRIBUTE, name);
	}

	public String getMachineClass() {
		return machineClass;
	}

	public void setMachineClass(String machineClass) {
		this.machineClass = machineClass;
		firePropertyChange(MACHINE_CLASS_ATTRIBUTE, machineClass);
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
		firePropertyChange(VENDOR_ATTRIBUTE, vendor);
	}

	public String getMachineModel() {
		return machineModel;
	}

	public void setMachineModel(String machineModel) {
		this.machineModel = machineModel;
		firePropertyChange(MACHINE_MODEL_ATTRIBUTE, machineModel);
	}

	public String getMachineType() {
		return machineType;
	}

	public void setMachineType(String machineType) {
		this.machineType = machineType;
		firePropertyChange(MACHINE_TYPE_ATTRIBUTE, machineType);
	}

	public String getWsPortNumber() {
		return wsPortNumber;
	}

	public void setWsPortNumber(String wsPortNumber) {
		this.wsPortNumber = wsPortNumber;
		firePropertyChange(WS_PORT_NUMBER_ATTRIBUTE, wsPortNumber);
	}
	
	public List<PeripheralModel> getPeripherals() {
		return peripherals;
	}
	
	public int getPeripheralCount() {
		return peripherals.size();
	}
	
	public Element exportElement() {
		Element result = DocumentHelper.createElement(DEVICE_ELEMENT);
		
		result.addAttribute(NAME_ATTRIBUTE, name);
		result.addAttribute(VENDOR_ATTRIBUTE, vendor);
		result.addAttribute(MACHINE_CLASS_ATTRIBUTE, machineClass);
		result.addAttribute(MACHINE_MODEL_ATTRIBUTE, machineModel);
		result.addAttribute(MACHINE_TYPE_ATTRIBUTE, machineType);
		result.addAttribute(WS_PORT_NUMBER_ATTRIBUTE, wsPortNumber);
		
		for (PeripheralModel peripheral : peripherals) {
			result.add(peripheral.exportElement());
		}
		
		return result;
	}
	
	private void loadFromElement(Element deviceElement) {
		name = deviceElement.attributeValue(NAME_ATTRIBUTE, "");
		vendor = deviceElement.attributeValue(VENDOR_ATTRIBUTE, DEFAULT_VENDOR);
		machineClass = deviceElement.attributeValue(MACHINE_CLASS_ATTRIBUTE, "");
		machineModel = deviceElement.attributeValue(MACHINE_MODEL_ATTRIBUTE, "");
		machineType = deviceElement.attributeValue(MACHINE_TYPE_ATTRIBUTE, DEFAULT_MACHINE_TYPE);
		wsPortNumber = deviceElement.attributeValue(WS_PORT_NUMBER_ATTRIBUTE, DEFAULT_WS_PORT_NUMBER);
		
		peripherals = PeripheralModel.loadFromParent(this, deviceElement);
	}
	
	public static DeviceModel load(CSModel cs, Element deviceElement) {
		DeviceModel result = new DeviceModel(cs);
		result.loadFromElement(deviceElement);
		return result;
	}

	public static List<DeviceModel> loadFromParent(CSModel cs, Element csElement) {
		List<DeviceModel> result = new LinkedList<DeviceModel>();
		List<Element> deviceElements = csElement.elements(DEVICE_ELEMENT);
		
		for (Element deviceElement : deviceElements) {
			result.add(load(cs, deviceElement));
		}
		
		return result;
	}

}

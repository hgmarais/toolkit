package com.s3.toolkit.cs.model;

import java.util.LinkedList;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.s3.toolkit.model.AbstractModel;
import com.s3.toolkit.model.Model;

public class PeripheralModel extends AbstractModel {
	
	public static final String PERIPHERAL_ELEMENT = "Peripheral";
	
	public static final String ID_ATTRIBUTE = "id";
	
	public static final String CONNECTION_ATTRIBUTE = "connection";
	
	public static final String MODBUS_DEVICE_NUM_ATTRIBUTE = "modbusDeviceNum";
	
	public static final String HIDDEN_ATTRIBUTE = "hidden";
	
	private String id = "";
	
	private String connection = "";
	
	private String modbusDeviceNum = "";
	
	private boolean hidden = false;
	
	private List<AbstractModel> parametersAndBlocks;
	
	public PeripheralModel(DeviceModel device) {
		super(device);
		parametersAndBlocks = new LinkedList<AbstractModel>();
	}
	
	@Override
	public DeviceModel getParent() {
		return (DeviceModel)super.getParent();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
		firePropertyChange(ID_ATTRIBUTE, id);
	}

	public String getConnection() {
		return connection;
	}

	public void setConnection(String connection) {
		this.connection = connection;
		firePropertyChange(CONNECTION_ATTRIBUTE, connection);
	}

	public String getModbusDeviceNum() {
		return modbusDeviceNum;
	}

	public void setModbusDeviceNum(String modbusDeviceNum) {
		this.modbusDeviceNum = modbusDeviceNum;
		firePropertyChange(MODBUS_DEVICE_NUM_ATTRIBUTE, modbusDeviceNum);
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
		firePropertyChange(HIDDEN_ATTRIBUTE, hidden);
	}
	
	public List<ParameterModel> getParameters() {
		List<ParameterModel> result = new LinkedList<ParameterModel>();
		
		for (Model model : parametersAndBlocks) {
			if (model.getClass() == ParameterModel.class) {
				result.add((ParameterModel) model);
			}
		}
		
		return result;
	}
	
	public List<BlockModel> getBlocks() {
		List<BlockModel> result = new LinkedList<BlockModel>();
		
		for (Model model : parametersAndBlocks) {
			if (model.getClass() == BlockModel.class) {
				result.add((BlockModel) model);
			}
		}
		
		return result;
	}

	public List<AbstractModel> getParametersAndBlocks() {
		return parametersAndBlocks;
	}
	
	public Element exportElement() {
		Element result = DocumentHelper.createElement(PERIPHERAL_ELEMENT);
		
		result.addAttribute(ID_ATTRIBUTE, id);
		result.addAttribute(CONNECTION_ATTRIBUTE, connection);
		result.addAttribute(MODBUS_DEVICE_NUM_ATTRIBUTE, modbusDeviceNum);
		result.addAttribute(HIDDEN_ATTRIBUTE, Boolean.toString(hidden));
		
		for (Model model : parametersAndBlocks) {
			if (model.getClass() == BlockModel.class) {
				result.add(((BlockModel)model).exportElement());
			} else {
				result.add(((ParameterModel)model).exportElement());
			}
		}
		
		return result;
	}
	
	private void loadFromElement(Element peripheralElement) {
		id = peripheralElement.attributeValue(ID_ATTRIBUTE, "");
		connection = peripheralElement.attributeValue(CONNECTION_ATTRIBUTE, "");
		modbusDeviceNum = peripheralElement.attributeValue(MODBUS_DEVICE_NUM_ATTRIBUTE, "");
		hidden = "true".equals(peripheralElement.attributeValue(HIDDEN_ATTRIBUTE, "false"));
		
		for (int i = 0; i < peripheralElement.nodeCount(); i++) {
			Node node = peripheralElement.node(i);
			
			if (!(node instanceof Element)) {
				continue;
			}
			
			String nodeName = node.getName();
			
			if (BlockModel.BLOCK_ELEMENT.equals(nodeName)) {
				parametersAndBlocks.add(BlockModel.load(this, (Element)node));
			} else if (ParameterModel.PARAMETER_ELEMENT.equals(nodeName)) {
				parametersAndBlocks.add(ParameterModel.load(this, (Element)node));
			}
		}
	}
	
	public static PeripheralModel load(DeviceModel device, Element peripheralElement) {
		PeripheralModel result = new PeripheralModel(device);
		result.loadFromElement(peripheralElement);
		return result;
	}

	public static List<PeripheralModel> loadFromParent(DeviceModel device, Element deviceElement) {
		List<PeripheralModel> result = new LinkedList<PeripheralModel>();
		List<Element> peripheralElements = deviceElement.elements(PERIPHERAL_ELEMENT);
		
		for (Element peripheralElement : peripheralElements) {
			result.add(load(device, peripheralElement));
		}
		
		return result;
	}
	
}

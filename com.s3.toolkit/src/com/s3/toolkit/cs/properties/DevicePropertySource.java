package com.s3.toolkit.cs.properties;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.s3.toolkit.cs.model.DeviceModel;

public class DevicePropertySource implements IPropertySource {

	private DeviceModel device;

	public DevicePropertySource(DeviceModel device) {
		this.device = device;
	}

	@Override
  public boolean isPropertySet(Object id) {
    return false;
  }

  @Override
  public Object getEditableValue() {
    return this;
  }

  @Override
  public IPropertyDescriptor[] getPropertyDescriptors() {
    return new IPropertyDescriptor[] {
        new TextPropertyDescriptor(DeviceModel.NAME_ATTRIBUTE, "Name"),
        new TextPropertyDescriptor(DeviceModel.VENDOR_ATTRIBUTE, "Vendor") };
  }

  @Override
  public Object getPropertyValue(Object id) {
    if (id.equals(DeviceModel.NAME_ATTRIBUTE)) {
      return device.getName();
    } else if (id.equals(DeviceModel.VENDOR_ATTRIBUTE)) {
      return device.getVendor();
    }
    
    return null;
  }

  @Override
  public void resetPropertyValue(Object id) {

  }

  @Override
  public void setPropertyValue(Object id, Object value) {
    String sValue = (String) value;
    
    if (id.equals(DeviceModel.NAME_ATTRIBUTE)) {
      device.setName(sValue);
    }
    
    if (id.equals(DeviceModel.VENDOR_ATTRIBUTE)) {
      device.setVendor(sValue);
    }
  }

}

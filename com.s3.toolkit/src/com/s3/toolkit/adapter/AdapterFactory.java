package com.s3.toolkit.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import com.s3.toolkit.cs.model.DeviceModel;
import com.s3.toolkit.cs.properties.DevicePropertySource;

public class AdapterFactory implements IAdapterFactory {

	@Override
  public Object getAdapter(Object adaptableObject, Class adapterType) {
		System.out.println("getAdapter : "+adapterType+" "+adaptableObject);
		
    if ((adapterType == IPropertySource.class) && (adaptableObject instanceof DeviceModel)) {
      return new DevicePropertySource((DeviceModel) adaptableObject);
    }
    
    return null;
  }

  @Override
  public Class[] getAdapterList() {
    return new Class[] { IPropertySource.class };
  }

}

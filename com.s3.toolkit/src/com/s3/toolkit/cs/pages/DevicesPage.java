package com.s3.toolkit.cs.pages;

import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.ScrolledPageBook;

import com.s3.toolkit.cs.editor.CSEditor;
import com.s3.toolkit.cs.model.DeviceModel;
import com.s3.toolkit.cs.model.PeripheralModel;

public class DevicesPage extends FormPage {

	public static final String ID = "devices";
	
	public static final String DEFAULT_PAGE_ID = "DEFAULT_PAGE";

	private DeviceDetailsPage devicePage;

	private PeripheralDetailsPage peripheralPage;

	private ScrolledPageBook pageBook;

	public DevicesPage(CSEditor editor) {
		super(editor, ID, "Devices");
		devicePage = new DeviceDetailsPage();
		peripheralPage = new PeripheralDetailsPage();
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();

		form.setText("Devices");
		toolkit.decorateFormHeading(form.getForm());
		
		form.getBody().setLayout(new MigLayout("novisualpadding, ins 0, fill"));
		pageBook = toolkit.createPageBook(form.getBody(), SWT.NONE);
		pageBook.setLayoutData("grow, push");
		Composite container = pageBook.getContainer();
		
		Composite defaultComposite = toolkit.createComposite(container); 
		Composite deviceComposite = toolkit.createComposite(container);
		Composite peripheralComposite = toolkit.createComposite(container);
		
		defaultComposite.setLayout(new GridLayout());
		
		Label label = toolkit.createLabel(defaultComposite, "Select a device or a peripheral from the outline view.", SWT.NONE);
		devicePage.initialize(managedForm, deviceComposite);
		peripheralPage.initialize(managedForm, peripheralComposite);
		
		GridData labelLayoutData = new GridData(GridData.FILL_BOTH);
		labelLayoutData.horizontalAlignment = SWT.CENTER;
		labelLayoutData.verticalAlignment = SWT.CENTER;
		label.setLayoutData(labelLayoutData);

		pageBook.registerPage(DEFAULT_PAGE_ID, defaultComposite);
		pageBook.registerPage(DeviceDetailsPage.class.getName(), deviceComposite);
		pageBook.registerPage(PeripheralDetailsPage.class.getName(), peripheralComposite);
		
		showDefaultPage();
	}
	
	public void showDefaultPage() {
		pageBook.showPage(DEFAULT_PAGE_ID);
	}

	public void showDevicePage(DeviceModel device) {
		devicePage.setDevice(device);
		pageBook.showPage(DeviceDetailsPage.class.getName());
	}
	
	public void showPeripheralPage(PeripheralModel peripheral) {
		peripheralPage.setPeripheral(peripheral);
		pageBook.showPage(PeripheralDetailsPage.class.getName());
	}

}

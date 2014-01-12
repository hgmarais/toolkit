package com.s3.toolkit.cs.pages;

import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import com.s3.toolkit.cs.model.DeviceModel;

public class DeviceDetailsPage {

	private IManagedForm form;
	
	private DeviceModel device;

	private Text nameText;

	private Text machineModelText;

	private Text machineTypeText;

	private Text machineClassText;

	private Text vendorText;

	private Text wsPortNumberText;

	private boolean refreshing = false;

	public void initialize(IManagedForm form, Composite parent) {
		this.form = form;
		parent.setLayout(new TableWrapLayout());
		
		addDeviceDetailsSection(parent);
		addMachineChangeDetectionSection(parent);
		addExpressionsSections(parent);
	}
	
	private void addDeviceDetailsSection(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		Section section = toolkit.createSection(parent, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED | Section.NO_TITLE_FOCUS_BOX);
		section.setText("Device Details");
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		
		Composite sectionClient = toolkit.createComposite(section, SWT.FILL);
		sectionClient.setLayout(new MigLayout("novisualpadding, ins 0, fill"));
		
		toolkit.createLabel(sectionClient, "Name");
		nameText = toolkit.createText(sectionClient, "");
		nameText.setLayoutData("pushx, growx, wrap");
		nameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!refreshing) {
					device.setName(nameText.getText());
				}
			}
		});
		
		toolkit.createLabel(sectionClient, "Vendor");
		vendorText = toolkit.createText(sectionClient, "");
		vendorText.setLayoutData("pushx, growx, wrap");
		vendorText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!refreshing) {
					device.setVendor(vendorText.getText());
				}
			}
		});
		
		toolkit.createLabel(sectionClient, "Machine Model");
		machineModelText = toolkit.createText(sectionClient, "");
		machineModelText.setLayoutData("pushx, growx, wrap");
		machineModelText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!refreshing) {
					device.setMachineModel(machineModelText.getText());
				}
			}
		});
		
		toolkit.createLabel(sectionClient, "Machine Type");
		machineTypeText = toolkit.createText(sectionClient, "");
		machineTypeText.setLayoutData("pushx, growx, wrap");
		machineTypeText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!refreshing) {
					device.setMachineType(machineTypeText.getText());
				}
			}
		});
		
		toolkit.createLabel(sectionClient, "Machine Class");
		machineClassText = toolkit.createText(sectionClient, "");
		machineClassText.setLayoutData("pushx, growx, wrap");
		machineClassText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!refreshing) {
					device.setMachineClass(machineClassText.getText());
				}
			}
		});
		
		toolkit.createLabel(sectionClient, "Weihenstephan Port Number");
		wsPortNumberText = toolkit.createText(sectionClient, "");
		wsPortNumberText.setLayoutData("pushx, growx, wrap");
		wsPortNumberText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!refreshing) {
					device.setWsPortNumber(wsPortNumberText.getText());
				}
			}
		});
		
		section.setClient(sectionClient);
	}

	private void addMachineChangeDetectionSection(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		Section section = toolkit.createSection(parent, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED | Section.NO_TITLE_FOCUS_BOX);
		section.setText("Machine Change Detection");
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		
		Composite sectionClient = toolkit.createComposite(section, SWT.FILL);
		sectionClient.setLayout(new MigLayout("novisualpadding, ins 0, fill"));
		section.setClient(sectionClient);
	}

	private void addExpressionsSections(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		Section section = toolkit.createSection(parent, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED | Section.NO_TITLE_FOCUS_BOX);
		section.setText("Expressions");
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		
		Composite sectionClient = toolkit.createComposite(section, SWT.FILL);
		sectionClient.setLayout(new MigLayout("novisualpadding, ins 0, fill"));
		section.setClient(sectionClient);
	}

	public void refresh() {
		refreshing = true;
		
		nameText.setText(device.getName());
		machineModelText.setText(device.getMachineModel());
		machineTypeText.setText(device.getMachineType());
		machineClassText.setText(device.getMachineClass());
		vendorText.setText(device.getVendor());
		wsPortNumberText.setText(device.getWsPortNumber());
		
		refreshing = false;
	}

	public void setDevice(DeviceModel device) {
		this.device = device;
		refresh();
	}

}

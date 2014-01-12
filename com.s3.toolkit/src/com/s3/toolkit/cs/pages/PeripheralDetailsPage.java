package com.s3.toolkit.cs.pages;

import java.util.List;

import net.miginfocom.swt.MigLayout;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import com.s3.toolkit.cs.CSUtils;
import com.s3.toolkit.cs.model.BlockModel;
import com.s3.toolkit.cs.model.CSModel;
import com.s3.toolkit.cs.model.ConnectionModel;
import com.s3.toolkit.cs.model.ParameterModel;
import com.s3.toolkit.cs.model.PeripheralModel;

public class PeripheralDetailsPage {
	
	private class ConnectionContentProvider implements IStructuredContentProvider {
		
		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			CSModel cs = peripheral.getParent().getParent();
			return cs.getConnections().getConnections().toArray();
		}
		
	}
	
	private class ConnectionLabelProvider extends LabelProvider {
		
		@Override
		public String getText(Object element) {
			if (element instanceof ConnectionModel) {
				return ((ConnectionModel) element).getConnName();
			}
			
			return super.getText(element);
		}
		
	}
	
	private class ParameterContentProvider implements IStructuredContentProvider {
		
		private List<ParameterModel> parameters;

		public ParameterContentProvider(List<ParameterModel> parameters) {
			this.parameters = parameters;
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return parameters.toArray();
		}
		
	}
	
	private class AddBlockSelectionListener implements SelectionListener {

		private BlockModel block;

		public AddBlockSelectionListener(BlockModel block) {
			this.block = block;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			System.out.println("Add Block");
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
		}
		
	}
	
	private class RemoveBlockSelectionListener implements SelectionListener {

		private BlockModel block;

		public RemoveBlockSelectionListener(BlockModel block) {
			this.block = block;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			System.out.println("Remove Block");
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
		}
		
	}
	
	private IManagedForm form;
	
	private PeripheralModel peripheral;
	
	private Text idText;

	private Combo connectionCombo;

	private ComboViewer connectionViewer;

	private Text modbusDeviceNumberText;

	private Button hiddenCheckbox;

	private boolean refreshing = true;

	private Composite blocksSectionClient;
	
	public void initialize(IManagedForm form, Composite parent) {
		this.form = form;
		parent.setLayout(new TableWrapLayout());
		addPeripheralDetailsSection(parent);
	}

	private void addPeripheralDetailsSection(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		Section section = toolkit.createSection(parent, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED | Section.NO_TITLE_FOCUS_BOX);
		section.setText("Peripheral Details");
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		
		Composite sectionClient = toolkit.createComposite(section, SWT.FILL);
		sectionClient.setLayout(new MigLayout("novisualpadding, ins 0, fill"));
		section.setClient(sectionClient);
		
		toolkit.createLabel(sectionClient, "ID");
		idText = toolkit.createText(sectionClient, "");
		idText.setLayoutData("pushx, growx, wrap");
		idText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!refreshing) {
					peripheral.setId(idText.getText());
				}
			}
		});
		
		toolkit.createLabel(sectionClient, "Connection");
		connectionCombo = new Combo(sectionClient, SWT.NONE);
		connectionCombo.setLayoutData("pushx, growx, wrap");
		connectionCombo.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!refreshing) {
					peripheral.setConnection(connectionCombo.getText());
				}
			}
		});
		
		connectionViewer = new ComboViewer(connectionCombo);
		connectionViewer.setContentProvider(new ConnectionContentProvider());
		connectionViewer.setLabelProvider(new ConnectionLabelProvider());
		
		toolkit.createLabel(sectionClient, "Modbus Device Number");
		modbusDeviceNumberText = toolkit.createText(sectionClient, "");
		modbusDeviceNumberText.setLayoutData("pushx, growx, wrap");
		modbusDeviceNumberText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!refreshing) {
					peripheral.setModbusDeviceNum(modbusDeviceNumberText.getText());
				}
			}
		});
		
		hiddenCheckbox = toolkit.createButton(sectionClient, "Hidden", SWT.CHECK);
		hiddenCheckbox.setLayoutData("spanx, pushx, growx, wrap");
		hiddenCheckbox.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				peripheral.setHidden(hiddenCheckbox.getSelection());
			}
			
		});

		blocksSectionClient = toolkit.createComposite(parent);
		blocksSectionClient.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		blocksSectionClient.setLayout(new MigLayout("novisualpadding, ins 0, fill"));
	}

	public void refresh() {
		refreshing = true;
		
		idText.setText(peripheral.getId());
		connectionViewer.setInput(peripheral);
		connectionCombo.setText(peripheral.getConnection());
		modbusDeviceNumberText.setText(peripheral.getModbusDeviceNum());
		hiddenCheckbox.setSelection(peripheral.isHidden());
		refreshBlocks();
		
		refreshing = false;
	}

	private void refreshBlocks() {
		for (Control child : blocksSectionClient.getChildren()) {
			if (!child.isDisposed()) {
				child.dispose();
			}
		}
		
		FormToolkit toolkit = form.getToolkit();
		
		for (BlockModel block : peripheral.getBlocks()) {
			Section blockSection = toolkit.createSection(blocksSectionClient, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
			blockSection.setLayoutData("pushx, growx, wrap");
			blockSection.setText("Block");
			
			Composite blockSectionClient = toolkit.createComposite(blockSection);
			blockSectionClient.setLayout(new MigLayout("novisualpadding, ins 0, fill"));
			blockSection.setClient(blockSectionClient);
			
			Composite blockComposite = toolkit.createComposite(blockSectionClient);
			blockComposite.setLayoutData("push, grow, wrap");
			blockComposite.setLayout(new MigLayout("novisualpadding, ins 0, fill"));
			
			toolkit.createLabel(blockComposite, "Name").setLayoutData("split 4, sg 1");
			toolkit.createText(blockComposite, block.getBlockName()).setLayoutData("sg 2");
			toolkit.createLabel(blockComposite, "Type").setLayoutData("sg 1");
			toolkit.createText(blockComposite, block.getBlockType()).setLayoutData("sg 2, wrap");
			
			Table parameterTable = toolkit.createTable(blockComposite, SWT.NONE);
			parameterTable.setLayoutData("spanx, push, grow, wrap");
			parameterTable.setHeaderVisible(true);
			TableViewer parameterViewer = new TableViewer(parameterTable);
			parameterViewer.setContentProvider(new ParameterContentProvider(block.getParameters()));
			CSUtils.addParameterColumns(parameterViewer);
			parameterViewer.setInput(block);
			
			/* Create tool bar. */
			ToolBar toolBar = new ToolBar(blockSection, SWT.FLAT | SWT.HORIZONTAL);
			createAddBlockToolItem(toolBar, block);
			createRemoveBlockToolItem(toolBar, block);
			blockSection.setTextClient(toolBar);
		}
		
		Button addBlockButton = toolkit.createButton(blocksSectionClient, "Add Block", SWT.NONE);
		addBlockButton.setLayoutData("spanx, ax center");
		
		blocksSectionClient.layout();
		blocksSectionClient.redraw();
	}

	private void createAddBlockToolItem(ToolBar toolBar, BlockModel block) {
//		ToolItem toolItem = new ToolItem(toolBar, SWT.PUSH);
//		toolItem.setText("+");
//		toolItem.setToolTipText("Add Block");
//		toolItem.addSelectionListener(new AddBlockSelectionListener(block));
	}
	
	private void createRemoveBlockToolItem(ToolBar toolBar, BlockModel block) {
		ToolItem toolItem = new ToolItem(toolBar, SWT.PUSH);
		toolItem.setText("-");
		toolItem.setToolTipText("Remove Block");
		toolItem.addSelectionListener(new RemoveBlockSelectionListener(block));
	}

	public void setPeripheral(PeripheralModel peripheral) {
		this.peripheral = peripheral;
		refresh();
	}
	
}

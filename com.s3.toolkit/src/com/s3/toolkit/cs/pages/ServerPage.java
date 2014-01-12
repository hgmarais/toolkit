package com.s3.toolkit.cs.pages;

import net.miginfocom.swt.MigLayout;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.IMessageManager;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import com.s3.toolkit.builder.FiltecBuilder;
import com.s3.toolkit.cs.editor.CSEditor;
import com.s3.toolkit.cs.model.CSModel;
import com.s3.toolkit.cs.model.CSGUIServerModel;
import com.s3.toolkit.cs.model.RMIServerModel;
import com.s3.toolkit.cs.model.ServerModel;
import com.s3.toolkit.utils.Utils;

public class ServerPage extends FormPage {

	public static final String ID = "server";
	
	private ServerModel server;

	private Text objBindNameText;

	public ServerPage(CSEditor editor) {
		super(editor, ID, "Server");
		CSModel connectivityServer = editor.getConnectivityServer();
		server = connectivityServer.getServer();
	}
	
	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		
		System.out.println("setActive : "+active);
		if (active) {
			refreshMarkers();
		} else {
			getManagedForm().getMessageManager().removeAllMessages();
		}
	}
	
	@Override
	public CSEditor getEditor() {
		return (CSEditor)super.getEditor();
	}
	
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		Composite body = form.getBody();

		TableWrapLayout layout = new TableWrapLayout();
		body.setLayout(layout);

		form.setText("Server");
		toolkit.decorateFormHeading(form.getForm());
		
		addGeneralSection(toolkit, body);
		addRmiServerSection(toolkit, body);
		addGuiServerSection(toolkit, body);
	}
	
	public void refreshMarkers() {
		if (!isActive()) {
			return;
		}
		
		IMessageManager messageManager = getManagedForm().getMessageManager();
		messageManager.removeAllMessages();
		
		IMarker[] markers = null;
		
		try {
			markers = getEditor().getFile().findMarkers(FiltecBuilder.MARKER_TYPE, true, IResource.DEPTH_INFINITE);
		} catch (CoreException e) {
			e.printStackTrace();
			return;
		}
		
		for (IMarker marker : markers) {
			String message = marker.getAttribute(IMarker.MESSAGE, "");
			int severity = marker.getAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
			String element = marker.getAttribute(FiltecBuilder.ELEMENT_ATTRIBUTE, "");
			int type = Utils.markerSeverityToMessageType(severity);
			
			System.out.println("refresh : "+element+" "+severity+" "+message);
			
			if (RMIServerModel.RMI_SERVER_ELEMENT.equals(element)) {
				String property = marker.getAttribute(FiltecBuilder.PROPERTY_ATTRIBUTE, "");
				
				System.out.println("property : "+property);
				if (RMIServerModel.OBJ_BIND_NAME_PROPERTY.endsWith(property)) {
					messageManager.addMessage(objBindNameText, message, null, type, objBindNameText);
				}
			}
		}
	}

	private void addGeneralSection(FormToolkit toolkit, Composite body) {
		final Button wsCheckbox = toolkit.createButton(body, "Weihenstephan Server", SWT.CHECK);
		final Button webServerCheckbox = toolkit.createButton(body, "Web Server", SWT.CHECK);
		final Button simulationModeCheckbox = toolkit.createButton(body, "Simulation Mode", SWT.CHECK);
		
		wsCheckbox.setSelection(server.isWeihenstephanServerEnabled());
		wsCheckbox.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				server.setWeihenstephanServerEnabled(wsCheckbox.getSelection());
			}
			
		});
		
		webServerCheckbox.setSelection(server.isWebServerEnabled());
		webServerCheckbox.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				server.setWebServerEnabled(webServerCheckbox.getSelection());
			}
			
		});
		
		simulationModeCheckbox.setSelection(server.isSimulationModeEnabled());
		simulationModeCheckbox.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				server.setSimulationModeEnabled(simulationModeCheckbox.getSelection());
			}
			
		});
	}
	
	private void addRmiServerSection(FormToolkit toolkit, Composite body) {
		RMIServerModel rmiServer = server.getRmiServer();
		
		Section section = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED | Section.NO_TITLE_FOCUS_BOX);
		section.setText("RMI Server");
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		
		Composite sectionClient = toolkit.createComposite(section, SWT.FILL);
		sectionClient.setLayout(new MigLayout("novisualpadding, ins 0, fill"));
		
		toolkit.createLabel(sectionClient, "Object Bind Name");
		objBindNameText = toolkit.createText(sectionClient, rmiServer.getObjBindName());
		objBindNameText.setLayoutData("gapleft 15px, pushx, growx, wrap");
		objBindNameText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				server.getRmiServer().setObjBindName(objBindNameText.getText());
			}
			
		});

		section.setClient(sectionClient);
	}
	
	private void addGuiServerSection(FormToolkit toolkit, Composite body) {
		CSGUIServerModel guiServer = server.getGuiServer();
		
		Section section = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED | Section.NO_TITLE_FOCUS_BOX);
		section.setText("GUI Server");
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite sectionClient = toolkit.createComposite(section, SWT.FILL);
		sectionClient.setLayout(new MigLayout("novisualpadding, ins 0, fill"));

		toolkit.createLabel(sectionClient, "Config");
		final Text configText = toolkit.createText(sectionClient, guiServer.getConfigXML());
		configText.setLayoutData("pushx, growx, wrap");
		configText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				server.getGuiServer().setConfigXML(configText.getText());
			}
			
		});
		
		toolkit.createLabel(sectionClient, "Server ID");
		final Text serverIdText = toolkit.createText(sectionClient, guiServer.getServerId());
		serverIdText.setLayoutData("pushx, growx, wrap");
		serverIdText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				server.getGuiServer().setServerId(serverIdText.getText());
			}
			
		});
	  
		toolkit.createLabel(sectionClient, "Port");
		final Text portNumberText = toolkit.createText(sectionClient, guiServer.getPortNumber(), SWT.FILL);
		portNumberText.setLayoutData("pushx, growx, wrap");
		portNumberText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				server.getGuiServer().setPortNumber(portNumberText.getText());
			}
			
		});
		
		section.setClient(sectionClient);
	}

}

package com.s3.toolkit.cs.editor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.dom4j.DocumentException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.s3.toolkit.cs.model.CSModel;
import com.s3.toolkit.cs.model.ConnectionsModel;
import com.s3.toolkit.cs.model.DeviceModel;
import com.s3.toolkit.cs.model.PeripheralModel;
import com.s3.toolkit.cs.model.ServerModel;
import com.s3.toolkit.cs.pages.ConnectionsPage;
import com.s3.toolkit.cs.pages.DBSinkGroupsPage;
import com.s3.toolkit.cs.pages.DevicesPage;
import com.s3.toolkit.cs.pages.ServerPage;
import com.s3.toolkit.model.Model;
import com.s3.toolkit.model.ModelEvent;
import com.s3.toolkit.model.ModelListener;
import com.s3.toolkit.utils.XMLUtils;

public class CSEditor extends FormEditor implements ModelListener {

	private CSModel connectivityServer;

	private boolean dirty = false;

	private CSOutlinePage outlinePage;

	private int serverPageIndex;

	private int connectionsPageIndex;

	private int dbSinkGroupsPageIndex;

	private int devicesPageIndex;

	private ServerPage serverPage;

	private DevicesPage devicesPage;

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);

		if (!(input instanceof FileEditorInput)) {
			throw new PartInitException("Only files can be opened.");
		}
		
		FileEditorInput fileInput = (FileEditorInput) input;
		InputStream is = null;
		connectivityServer = new CSModel();

		try {
			is = fileInput.getStorage().getContents();
			XMLUtils.importElement(connectivityServer, is);
			connectivityServer.addListener(this);
		} catch (DocumentException e) {
			throw new PartInitException("Could not read file.", e);
		} catch (CoreException e) {
			throw new PartInitException("Could not read file.", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.addResourceChangeListener(new IResourceChangeListener() {
			
			@Override
			public void resourceChanged(IResourceChangeEvent event) {
				System.out.println("resourceChanged : "+event.getType()+" "+event.getBuildKind()+" "+event.getResource()+" "+event.getDelta());
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						serverPage.refreshMarkers();
					}
				});
			}
			
		}, IResourceChangeEvent.POST_BUILD);
	}
	
	public CSModel getConnectivityServer() {
		return connectivityServer;
	}
	
	public IFile getFile() {
		FileEditorInput fileInput = (FileEditorInput) getEditorInput();
		return fileInput.getFile();
	}

	@Override
	protected void addPages() {
		try {
			serverPage = new ServerPage(this);
			devicesPage = new DevicesPage(this);
			serverPageIndex = addPage(serverPage);
			connectionsPageIndex = addPage(new ConnectionsPage(this));
			dbSinkGroupsPageIndex = addPage(new DBSinkGroupsPage(this));
			devicesPageIndex = addPage(devicesPage);
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), "Error creating nested text editor", null, e.getStatus());
		}
		
		
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		IFile file = ((IFileEditorInput) getEditorInput()).getFile();
		try {
			byte[] bytes = XMLUtils.exportBytes(XMLUtils.exportDocument(connectivityServer.exportElement()));
			System.out.println("Saved : ");
			System.out.println(new String(bytes));
			file.setContents(new ByteArrayInputStream(bytes), true, false, monitor);
			dirty = false;
			firePropertyChange(IEditorPart.PROP_DIRTY);
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void handleEvent(ModelEvent event) {
		if (!dirty) {
			dirty = true;
			firePropertyChange(IEditorPart.PROP_DIRTY);
		}
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	public Object getAdapter(Class required) {
		if (IContentOutlinePage.class.equals(required)) {
			if (outlinePage == null) {
				outlinePage = new CSOutlinePage(connectivityServer);
				outlinePage.addSelectionChangedListener(new ISelectionChangedListener() {

					
					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						IStructuredSelection selection = (IStructuredSelection)event.getSelection();
						
						if (selection.isEmpty()) {
							return;
						}
						
						Object element = selection.getFirstElement();
						Class<? extends Object> c = element.getClass();
						
						if (c == DeviceModel.class) {
							setActivePage(devicesPageIndex);
							devicesPage.showDevicePage((DeviceModel)element);
							getSite().getSelectionProvider().setSelection(selection);
							return;
						} else if (c == PeripheralModel.class) {
							setActivePage(devicesPageIndex);
							devicesPage.showPeripheralPage((PeripheralModel)element);
							getSite().getSelectionProvider().setSelection(selection);
							return;
						} else {
							outlineSelectionChanged(element);
							getSite().getSelectionProvider().setSelection(StructuredSelection.EMPTY);
						}
					}
					
				});
			}

			return outlinePage;
		}
		
		return super.getAdapter(required);
	}
	
	private void outlineSelectionChanged(Object element) {
		Class<? extends Object> c = element.getClass();
		
		if (c == ServerModel.class) {
			setActivePage(serverPageIndex);
		} else if (c == ConnectionsModel.class) {
			setActivePage(connectionsPageIndex);
		} else if (element instanceof Model) {
			Model model = (Model)element;
			Model parent = model.getParent();
			
			if (parent != null) {
				outlineSelectionChanged(parent);
			}
		} else if (element == CSOutlinePage.DEVICES_ELEMENT) {
			setActivePage(devicesPageIndex);
			devicesPage.showDefaultPage();
		}
	}
	
}

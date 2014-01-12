package com.s3.toolkit.cs.editor;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import com.s3.toolkit.cs.model.CSGUIServerModel;
import com.s3.toolkit.cs.model.CSModel;
import com.s3.toolkit.cs.model.ConnectionsModel;
import com.s3.toolkit.cs.model.DeviceModel;
import com.s3.toolkit.cs.model.PeripheralModel;
import com.s3.toolkit.cs.model.RMIServerModel;
import com.s3.toolkit.cs.model.ServerModel;
import com.s3.toolkit.model.ModelEvent;
import com.s3.toolkit.model.ModelListener;
import com.s3.toolkit.model.PropertyChangeEvent;

public class CSOutlinePage extends ContentOutlinePage implements ModelListener {
	
	public static final String DEVICES_ELEMENT = "Devices";
	
	private class CSContentProvider implements ITreeContentProvider {
		
		private CSModel input;

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			input = (CSModel) newInput;
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return new Object[]{input.getServer(), input.getConnections(), DEVICES_ELEMENT};
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof ServerModel) {
				ServerModel server = (ServerModel)parentElement;
				return new Object[]{server.getRmiServer(), server.getGuiServer()};
			} else if (parentElement == DEVICES_ELEMENT) {
				return input.getDevices().toArray();
			} else if (parentElement instanceof DeviceModel) {
				return ((DeviceModel)parentElement).getPeripherals().toArray();
			}
			
			return null;
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			if (element instanceof ServerModel) {
				return true;
			} else if (element == DEVICES_ELEMENT) {
				return !input.getDevices().isEmpty();
			} else if (element instanceof DeviceModel) {
				return ((DeviceModel)element).getPeripheralCount() > 0;
			}
			
			return false;
		}
		
	}
	
	private class CSLabelProvider extends LabelProvider {
		
		@Override
		public String getText(Object element) {
			Class<? extends Object> c = element.getClass();
			
			if (c == ServerModel.class) {
				return "Server";
			} else if (c == ConnectionsModel.class) {
				return "Connections";
			} else if (c == CSGUIServerModel.class) {
				return "GUI Server";
			} else if (c == RMIServerModel.class) {
				return "RMI Server";
			} else if (c == DeviceModel.class) {
				return ((DeviceModel)element).getName();
			} else if (c == PeripheralModel.class) {
				return ((PeripheralModel)element).getId();
			}
			
			return super.getText(element);
		}
		
	}

	private CSModel connectivityServer;
	
	public CSOutlinePage(CSModel connectivityServer) {
		this.connectivityServer = connectivityServer; 
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		TreeViewer treeViewer = getTreeViewer();

		treeViewer.setContentProvider(new CSContentProvider());
		treeViewer.setLabelProvider(new CSLabelProvider());
		treeViewer.setInput(connectivityServer);

		getSite().setSelectionProvider(treeViewer);
		
		connectivityServer.addListener(this);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		connectivityServer.removeListener(this);
	}

	@Override
	public void handleEvent(ModelEvent event) {
		if (event instanceof PropertyChangeEvent) {
			getTreeViewer().update(((PropertyChangeEvent)event).getModel(), null);
		}
	}

}

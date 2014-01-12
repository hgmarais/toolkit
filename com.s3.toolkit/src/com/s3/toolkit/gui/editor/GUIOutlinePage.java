package com.s3.toolkit.gui.editor;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import com.s3.toolkit.gui.model.GUIDeviceModel;
import com.s3.toolkit.gui.model.GUIServerModel;
import com.s3.toolkit.gui.model.GroupModel;
import com.s3.toolkit.gui.model.PageModel;

public class GUIOutlinePage extends ContentOutlinePage {
	
	private GUIServerModel guiServer;
	
	private class GUIContentProvider implements ITreeContentProvider {
		
		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return guiServer.getDevices().toArray();
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			Class<? extends Object> c = parentElement.getClass();
			
			if (c == GUIDeviceModel.class) {
				GUIDeviceModel guiDevice = (GUIDeviceModel)parentElement;
				return guiDevice.getChildren().toArray();
			} else if (c == PageModel.class) {
				PageModel page = (PageModel)parentElement;
				return page.getGroups().toArray();
			}
//			if (parent.equals("FT50_01") || parent.equals("FT50_02")) {
//				return new Object[]{"Menu (Page)", DASHBOARD_ELEMENT};
//			} else if (parent.equals("Menu (Page)")) {
//				return new Object[]{"Reporting (Group)", "Production (Group)", "System (Group)", "Access (Group)"};
//			} else if (parent.equals("Reporting (Group)")) {
//				return new Object[]{"Cluster Counters (Page)", "Counters Menu (Page)", "Event Log (Page)"};
//			}
			
			return null;
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			return true;
//			String e = String.valueOf(element);
//			return !(e.equals("Cluster Counters (Page)") || e.equals("Counters Menu (Page)") || e.equals("Event Log (Page)") || e.equals(DASHBOARD_ELEMENT));
		}
		
	}
	
	private class GUILabelProvider extends LabelProvider {
		
		@Override
		public String getText(Object element) {
			Class<? extends Object> c = element.getClass();
			
			if (c == GUIDeviceModel.class) {
				return ((GUIDeviceModel)element).getDeviceName();
			} else if (c == PageModel.class) {
				return ((PageModel)element).getName()+" (Page)";
			} else if (c == GroupModel.class) {
				return ((GroupModel)element).getName() + " (Group)";
			}
			
			return String.valueOf(element);
		}
		
	}

	public GUIOutlinePage(GUIServerModel gui) {
		this.guiServer = gui;
	}
	
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		TreeViewer treeViewer = getTreeViewer();

		treeViewer.setContentProvider(new GUIContentProvider());
		treeViewer.setLabelProvider(new GUILabelProvider());
		treeViewer.setInput(guiServer);

		getSite().setSelectionProvider(treeViewer);
	}

}

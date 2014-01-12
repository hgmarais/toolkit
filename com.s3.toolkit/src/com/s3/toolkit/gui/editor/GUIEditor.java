package com.s3.toolkit.gui.editor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.dom4j.DocumentException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.s3.toolkit.Activator;
import com.s3.toolkit.gui.model.GUIServerModel;
import com.s3.toolkit.model.ModelEvent;
import com.s3.toolkit.model.ModelListener;
import com.s3.toolkit.utils.XMLUtils;

public class GUIEditor extends EditorPart implements ModelListener {

	private GUIServerModel guiServer;
	
	private boolean dirty = false;
	
	private Image image;
	
	private GUIOutlinePage outlinePage;

	public GUIEditor() {
		System.out.println("GUIEditor");
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		IFile file = ((IFileEditorInput) getEditorInput()).getFile();
		try {
			byte[] bytes = XMLUtils.exportBytes(XMLUtils.exportDocument(guiServer.exportElement()));
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
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if (!(input instanceof FileEditorInput)) {
			throw new PartInitException("Only files can be opened.");
		}
		
		FileEditorInput fileInput = (FileEditorInput) input;
		InputStream is = null;
		
		guiServer = new GUIServerModel();

		try {
			is = fileInput.getStorage().getContents();
			XMLUtils.importElement(guiServer, is);
			guiServer.addListener(this);
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
		
		setSite(site);
		setInput(input);
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		ScrolledComposite sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
    
		IPath path = new Path("images/GUI.png");
    URL url = FileLocator.find(Activator.getDefault().getBundle(), path, null);
    ImageDescriptor descriptor = ImageDescriptor.createFromURL(url);
    image = descriptor.createImage();
    
    Label label = new Label(sc, SWT.NONE);
		label.setSize(image.getImageData().width, image.getImageData().height);
    label.setImage(image);
    
    sc.setContent(label);
	}

	@Override
	public void setFocus() {
	}
	
	@Override
	public void dispose() {
		super.dispose();
		image.dispose();
	}
	
	public Object getAdapter(Class required) {
		if (IContentOutlinePage.class.equals(required)) {
			if (outlinePage == null) {
				outlinePage = new GUIOutlinePage(guiServer);
				outlinePage.addSelectionChangedListener(new ISelectionChangedListener() {

					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						IStructuredSelection selection = (IStructuredSelection)event.getSelection();
						
						if (selection.isEmpty()) {
							return;
						}
						
//						outlineSelectionChanged(selection.getFirstElement());
					}
					
				});
			}

			return outlinePage;
		}
		
		return super.getAdapter(required);
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

}

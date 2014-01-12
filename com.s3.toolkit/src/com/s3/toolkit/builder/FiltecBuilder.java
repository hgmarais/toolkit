package com.s3.toolkit.builder;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.DocumentException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.s3.toolkit.Activator;
import com.s3.toolkit.cs.model.CSModel;
import com.s3.toolkit.cs.model.ConnectionModel;
import com.s3.toolkit.cs.model.ConnectionsModel;
import com.s3.toolkit.cs.model.DeviceModel;
import com.s3.toolkit.cs.model.CSGUIServerModel;
import com.s3.toolkit.cs.model.PeripheralModel;
import com.s3.toolkit.cs.model.RMIServerModel;
import com.s3.toolkit.cs.model.ServerModel;
import com.s3.toolkit.utils.XMLUtils;

public class FiltecBuilder extends IncrementalProjectBuilder {
	
	public static final String ELEMENT_ATTRIBUTE = "ELEMENT";
	
	public static final String PROPERTY_ATTRIBUTE = "PROPERTY";

	class SampleDeltaVisitor implements IResourceDeltaVisitor {
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
		 */
		public boolean visit(IResourceDelta delta) throws CoreException {
			IResource resource = delta.getResource();
			switch (delta.getKind()) {
			case IResourceDelta.ADDED:
				// handle added resource
				checkXML(resource);
				break;
			case IResourceDelta.REMOVED:
				// handle removed resource
				break;
			case IResourceDelta.CHANGED:
				// handle changed resource
				checkXML(resource);
				break;
			}
			//return true to continue visiting children.
			return true;
		}
	}

	class SampleResourceVisitor implements IResourceVisitor {
		public boolean visit(IResource resource) {
			try {
				checkXML(resource);
			} catch (CoreException e) {
				e.printStackTrace();
			}
			//return true to continue visiting children.
			return true;
		}
	}

	class XMLErrorHandler extends DefaultHandler {
		
		private IFile file;

		public XMLErrorHandler(IFile file) {
			this.file = file;
		}

		private void addMarker(SAXParseException e, int severity) {
//			FiltecBuilder.this.addMarker(file, e.getMessage(), e
//					.getLineNumber(), severity);
		}

		public void error(SAXParseException exception) throws SAXException {
			addMarker(exception, IMarker.SEVERITY_ERROR);
		}

		public void fatalError(SAXParseException exception) throws SAXException {
			addMarker(exception, IMarker.SEVERITY_ERROR);
		}

		public void warning(SAXParseException exception) throws SAXException {
			addMarker(exception, IMarker.SEVERITY_WARNING);
		}
	}

	public static final String BUILDER_ID = "com.s3.toolkit.filtecBuilder";

	public static final String MARKER_TYPE = "com.s3.toolkit.xmlProblem";

//	private SAXParserFactory parserFactory;

	private IFile file;

	private IMarker addMarker(String message, int severity) throws CoreException {
		System.out.println("addMarker : "+message+" "+file);
		IMarker marker = file.createMarker(MARKER_TYPE);
		marker.setAttribute(IMarker.MESSAGE, message);
		marker.setAttribute(IMarker.SEVERITY, severity);
		return marker;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.internal.events.InternalBuilder#build(int,
	 *      java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {
		System.out.println("build : "+kind);
		if (kind == FULL_BUILD) {
			fullBuild(monitor);
		} else {
			IResourceDelta delta = getDelta(getProject());
			System.out.println("delta : "+delta);
			if (delta == null) {
				fullBuild(monitor);
			} else {
				incrementalBuild(delta, monitor);
			}
		}
		return null;
	}

	void checkXML(IResource resource) throws CoreException {
		System.out.println("checkXML : "+resource.getName()+" "+resource.getProjectRelativePath());
		if (resource instanceof IFile) {
			file = (IFile) resource;
			if (file.equals(getProject().getFile("CS_Config.xml"))) {
				System.out.println("validate");
				deleteMarkers(file);
				CSModel cs = loadConnectivityServer(file);
				validateCS(cs);
			}
//			XMLErrorHandler reporter = new XMLErrorHandler(file);
//			try {
//				getParser().parse(file.getContents(), reporter);
//			} catch (Exception e1) {
//			}
		}
	}

	private void validateCS(CSModel cs) throws CoreException {
		System.out.println("validateCS");
		validateServer(cs.getServer());
		validateConnections(cs.getConnections());
		validateDevices(cs.getDevices());
	}

	private void validateServer(ServerModel server) throws CoreException {
		validateGUIServer(server.getGuiServer());
		validateRMIServer(server.getRmiServer());
	}

	private void validateGUIServer(CSGUIServerModel guiServer) throws CoreException {
		String configXML = guiServer.getConfigXML();
		
		if ((configXML == null) || configXML.trim().isEmpty()) {
			addMarker("The path of the GUI server's config XML file hasn't been specified.", IMarker.SEVERITY_WARNING);
		}
	}

	private void validateRMIServer(RMIServerModel rmiServer) throws CoreException {
		String objBindName = rmiServer.getObjBindName();
		
		if ((objBindName == null) || objBindName.trim().isEmpty()) {
			IMarker marker = addMarker("The RMI server's Object Bind Name hasn't been specified.", IMarker.SEVERITY_WARNING);
			marker.setAttribute(ELEMENT_ATTRIBUTE, RMIServerModel.RMI_SERVER_ELEMENT);
			marker.setAttribute(PROPERTY_ATTRIBUTE, RMIServerModel.OBJ_BIND_NAME_PROPERTY);
		}
	}

	private void validateConnections(ConnectionsModel connections) throws CoreException {
		List<ConnectionModel> list = connections.getConnections();
		
		if (list.isEmpty()) {
			addMarker("There are no connections defined.", IMarker.SEVERITY_WARNING);
		} else {
			Set<String> names = new HashSet<String>();
			Set<String> duplicateNames = new HashSet<String>();
			
			for (ConnectionModel connection : list) {
				String name = connection.getConnName();
				
				if (!names.add(name)) {
					if (duplicateNames.add(name)) {
						addMarker("There is more than one connection with the name '"+name+"'.", IMarker.SEVERITY_ERROR);
					}
				}
			}
		}
	}

	private void validateDevices(List<DeviceModel> devices) throws CoreException {
		if (devices.isEmpty()) {
			addMarker("There are no devices defined.", IMarker.SEVERITY_WARNING);
			return;
		}
		
		for (DeviceModel device : devices) {
			validateDevice(device);
		}
	}

	private void validateDevice(DeviceModel device) throws CoreException {
		String name = device.getName();
		
		if (name.isEmpty()) {
			addMarker("Device name may not be empty.", IMarker.SEVERITY_ERROR);
		}
		
		validatePeripherals(device, device.getPeripherals());
	}

	private void validatePeripherals(DeviceModel device, List<PeripheralModel> peripherals) throws CoreException {
		if (peripherals.isEmpty()) {
			addMarker("There are no peripherals defined for device "+device.getName()+".", IMarker.SEVERITY_WARNING);
			return;
		}
		
		for (PeripheralModel peripheral : peripherals) {
			validatePeripheral(peripheral);
		}
	}

	private void validatePeripheral(PeripheralModel peripheral) throws CoreException {
		String name = peripheral.getId();
		boolean nameEmpty = name.isEmpty();
		
		if (nameEmpty) {
			addMarker("Peripheral ID may not be empty.", IMarker.SEVERITY_ERROR);
		}
		
		String connection = peripheral.getConnection();
		
		if (connection.isEmpty()) {
			addMarker("Connection not specified for peripheral "+name, IMarker.SEVERITY_ERROR);
		} else {
			CSModel cs = peripheral.getParent().getParent();
			if (cs.getConnections().getConnection(connection) == null) {
				addMarker("A connection with name '"+connection+"' does not exist.", IMarker.SEVERITY_ERROR);
			}
		}
	}

	private CSModel loadConnectivityServer(IFile file) throws CoreException {
		InputStream is = null;
		CSModel result = new CSModel();

		try {
			is = file.getContents();
			XMLUtils.importElement(result, is);
		} catch (DocumentException e) {
			throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error loading file.", e));
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}

	private void deleteMarkers(IFile file) {
		try {
			file.deleteMarkers(MARKER_TYPE, false, IResource.DEPTH_ZERO);
		} catch (CoreException ce) {
		}
	}

	protected void fullBuild(final IProgressMonitor monitor)
			throws CoreException {
		try {
			getProject().accept(new SampleResourceVisitor());
		} catch (CoreException e) {
		}
	}

//	private SAXParser getParser() throws ParserConfigurationException,
//			SAXException {
//		if (parserFactory == null) {
//			parserFactory = SAXParserFactory.newInstance();
//		}
//		return parserFactory.newSAXParser();
//	}

	protected void incrementalBuild(IResourceDelta delta,
			IProgressMonitor monitor) throws CoreException {
		// the visitor does the work.
		delta.accept(new SampleDeltaVisitor());
	}
}

package com.s3.toolkit.cs.model;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.s3.toolkit.model.AbstractModel;


public class ServerModel extends AbstractModel {
	
	public static final String SERVER_ELEMENT = "Server";
	
	public static final String WEIHENSTEPHEN_SERVER_ELEMENT = "WeihenstephanServer";
	
	public static final String WEB_SERVER_ELEMENT = "WebServer";
	
	public static final String SIMULATION_MODE_ELEMENT = "SimulationMode";
	
	public static final String WEIHENSTEPHAN_SERVER_ENABLED_PROPERTY = "weihenstephanServerEnabled";
	
	public static final String WEB_SERVER_ENABLED_PROPERTY = "webServerEnabled";
	
	public static final String SIMULATION_MODE_ENABLED_PROPERTY = "webServerEnabled";

	private boolean weihenstephanServerEnabled = false;
	
	private boolean webServerEnabled = false;
	
	private boolean simulationModeEnabled = false;
	
	private RMIServerModel rmiServer;
	
	private CSGUIServerModel guiServer;
	
	public ServerModel(CSModel parent) {
		super(parent);
		rmiServer = new RMIServerModel(this);
		guiServer = new CSGUIServerModel(this);
	}
	
	public void setWeihenstephanServerEnabled(boolean enabled) {
		if (weihenstephanServerEnabled == enabled) {
			return;
		}
		
		weihenstephanServerEnabled = enabled;
		firePropertyChange(WEIHENSTEPHAN_SERVER_ENABLED_PROPERTY, weihenstephanServerEnabled);
	}
	
	public boolean isWeihenstephanServerEnabled() {
		return weihenstephanServerEnabled;
	}
	
	public boolean isWebServerEnabled() {
		return webServerEnabled;
	}
	
	public void setWebServerEnabled(boolean webServerEnabled) {
		if (this.webServerEnabled == webServerEnabled) {
			return;
		}
		
		this.webServerEnabled = webServerEnabled;
		firePropertyChange(WEB_SERVER_ENABLED_PROPERTY, webServerEnabled);
	}
	
	public boolean isSimulationModeEnabled() {
		return simulationModeEnabled;
	}
	
	public void setSimulationModeEnabled(boolean simulationModeEnabled) {
		if (this.simulationModeEnabled == simulationModeEnabled) {
			return;
		}
		
		this.simulationModeEnabled = simulationModeEnabled;
		firePropertyChange(SIMULATION_MODE_ENABLED_PROPERTY, simulationModeEnabled);
	}
	
	public RMIServerModel getRmiServer() {
		return rmiServer;
	}
	
	public CSGUIServerModel getGuiServer() {
		return guiServer;
	}

	public void loadFromParent(Element csElement) {
		Element serverElement = csElement.element(SERVER_ELEMENT);
		
		if (serverElement == null) {
			return;
		}
		
		weihenstephanServerEnabled = serverElement.element(WEIHENSTEPHEN_SERVER_ELEMENT) != null;
		webServerEnabled = serverElement.element(WEB_SERVER_ELEMENT) != null;
		simulationModeEnabled = serverElement.element(SIMULATION_MODE_ELEMENT) != null;
		rmiServer.loadFromParent(serverElement);
		guiServer.loadFromParent(serverElement);
	}

	public Element exportElement() {
		Element result = DocumentHelper.createElement(SERVER_ELEMENT);
		
		if (weihenstephanServerEnabled) {
			result.addElement(WEIHENSTEPHEN_SERVER_ELEMENT);
		}
		
		if (rmiServer.isEnabled()) {
			result.add(rmiServer.exportElement());
		}
		
		if (guiServer.isEnabled()) {
			result.add(guiServer.exportElement());
		}
		
		if (simulationModeEnabled) {
			result.addElement(SIMULATION_MODE_ELEMENT);
		}
		
		if (webServerEnabled) {
			result.addElement(WEB_SERVER_ELEMENT);
		}
		
		return result;
	}

}

package com.s3.toolkit;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder = { "simulationMode", "weihenstephanServer" })
public class Server {
	
	@XmlElement(name="WeihenstephanServer", required=false)
	private String weihenstephanServer = "";
	
	@XmlElement(name="SimulationMode", required=false)
	private String simulationMode = "";

}

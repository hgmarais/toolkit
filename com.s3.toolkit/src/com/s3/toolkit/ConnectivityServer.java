package com.s3.toolkit;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ConnectivityServer")
public class ConnectivityServer {
	
	@XmlElement(name="Server")
	private Server server = new Server();
	

}

package com.s3.toolkit;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.s3.toolkit.cs.model.ServerModel;

public class TestXML {
	
	public static void main(String[] args) throws DocumentException, IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\"?>\n");
		sb.append("<!DOCTYPE ConnectivityServer SYSTEM \"CS_Config.dtd\">\n");
		sb.append("<ConnectivityServer>\n");
		sb.append("<Server>\n");
		sb.append("<!--Comment1-->\n");
		sb.append("<").append(ServerModel.WEIHENSTEPHEN_SERVER_ELEMENT).append("/>\n");
		sb.append("<!--Comment2-->\n");
		sb.append("<RMIServer objBindName=\"FT70_CS\">\n");
		sb.append("<!--Comment3-->\n");
		sb.append("</RMIServer>\n");
		sb.append("</Server>\n");
		sb.append("   <!--Comment-->\n\n");
		sb.append("</ConnectivityServer>");
		
		System.out.println(sb);
		
		ByteArrayInputStream inputStream = new ByteArrayInputStream(sb.toString().getBytes());
		SAXReader reader = new SAXReader(false);
		reader.setEntityResolver(new EntityResolver() {
			
			@Override
			public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
				if (systemId.contains("CS_Config.dtd")) {
					return new InputSource("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!ELEMENT ConnectivityServer>");
				}
				return null;
			}
		});
		
		Document document = reader.read(inputStream);
		

	}

}

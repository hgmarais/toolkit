package com.s3.toolkit.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.s3.toolkit.model.XMLModel;

public class XMLUtils {
	
	public static Document exportDocument(XMLModel model) {
		return exportDocument(model.exportElement());
	}

	public static Document exportDocument(Element element) {
		Document document = DocumentHelper.createDocument();
		document.add(element);
		return document;
	}
	
	public static byte[] exportBytes(Document document) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer = new XMLWriter(outputStream, format);
		
		writer.write(document);
		writer.flush();
		
		return outputStream.toByteArray();
	}
	
	public static String exportXML(Document document) throws IOException {
		return new String(exportBytes(document));
	}
	
	public static void importElement(XMLModel model, InputStream inputStream) throws DocumentException {
		importElement(model, model.getElementName(), inputStream);
	}
	
	public static void importElement(XMLModel model, String rootElementName, InputStream inputStream) throws DocumentException {
		SAXReader reader = new SAXReader(false);
		Document document = reader.read(inputStream);
		Element root = document.getRootElement();
		
		if ((rootElementName != null) && !rootElementName.equals(root.getName())) {
			throw new DocumentException(rootElementName + " element not found.");
		}
		
		model.importElement(root);
	}
	
}

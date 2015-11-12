package main.java.nl.tue.ieis.get.xml.utils;

import java.io.*;

import org.w3c.dom.*;

import javax.xml.parsers.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

public class CreateXML{

    public static void main (String args[]) throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException {
       CreateXML test = new CreateXML("testrootElement");
       Document d = test.getDoc();
       //Element root = test.getRootElement();
       Element root = d.createElement("rootElement");
       Element child = d.createElement("child");
       root.appendChild(child);
       Text text = d.createTextNode("My node value");
       child.appendChild(text);
       System.out.println("xml output is:\n\n" + createXMLString(d));
    }
    
    private Document doc;
    private Element rootElement;
    
    public Document getDoc() {
    	return doc;
    }
    
    public Element getRootElement(){
    	return rootElement;
    }

    public CreateXML(String routeName) {
    	try {
    		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            this.doc = docBuilder.newDocument();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

	/**
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerConfigurationException
	 * @throws TransformerException
	 */
	private static String createXMLString(Document document) {
		//set up a transformer
		TransformerFactory transfac = TransformerFactory.newInstance();
		Transformer trans;
		String xmlString = "No Result";
		try {
			trans = transfac.newTransformer();
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			trans.setOutputProperty(OutputKeys.INDENT, "yes");

			//create string from xml tree
			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			DOMSource source = new DOMSource(document);
			trans.transform(source, result);
			xmlString = sw.toString();
		} catch (TransformerException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return xmlString;
	}
}
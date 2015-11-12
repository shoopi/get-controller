package main.java.nl.tue.ieis.get.xml.utils;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;



public class ReadXMLFile {

	public List<Element> readFile (String xmlFile, String xpath, boolean urlAddress) {
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		List<Element> selected = null;
			try {
				if(urlAddress) {
					URL url = new URL(xmlFile);
					doc = builder.build(url);
				} else {
					doc = builder.build(new StringReader(xmlFile));
				}
				
				XPathExpression<Element> xpathExpression;
				boolean nameSpace=false;
				if(nameSpace) {
					Namespace yawlNS = Namespace.getNamespace("x","http://www.yawlfoundation.org/yawlschema");
					xpathExpression = XPathFactory.instance().compile(xpath, Filters.element(), null, yawlNS);
				} else {
					xpathExpression = XPathFactory.instance().compile(xpath, Filters.element(), null);
				}
				
				//xpathExpression.setVariable("x", defaultNS);
				//p1.addNamespace(defaultNS);
				//selected = p1.selectNodes(doc);
				selected = xpathExpression.evaluate(doc);

			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("IOException Exception \n" + e.getMessage());
			} catch (JDOMException e) {
				System.out.println("JDOM Exception \n" + e.getMessage());
				e.printStackTrace();
			}
		return selected;
	}
}
 
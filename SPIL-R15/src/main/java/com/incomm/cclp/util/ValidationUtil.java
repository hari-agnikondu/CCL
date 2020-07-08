package com.incomm.cclp.util;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Component
public class ValidationUtil {

	@Value("${cclp.supportlog.securedElements}")
	private String securedXmlElements;

	private static final Logger logger = LogManager.getLogger(ValidationUtil.class);

	private Document getXmlDocument(final String inXml) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder db = factory.newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(inXml));
		return db.parse(is);
	}

	private Document parseAndMaskData(final String inXml) throws ParserConfigurationException, SAXException, IOException {
		String strnode = null;
		String strval = null;
		Element element = null;
		String strcmp = null;
		Document doc = null;
		NodeList nodeList = null;

		try {
			doc = getXmlDocument(inXml);
			nodeList = doc.getElementsByTagName("*");

			for (int i = 0; i < nodeList.getLength(); i++) {
				try {
					element = (Element) nodeList.item(i);
					if (element.getNodeName()
						.equalsIgnoreCase("Extension")) {
						element = (Element) nodeList.item(i + 1);
						strnode = element.getChildNodes()
							.item(0) == null ? ""
									: element.getChildNodes()
										.item(0)
										.getNodeValue();
						element = (Element) nodeList.item(i + 2);
						strval = element.getChildNodes()
							.item(0) == null ? ""
									: element.getChildNodes()
										.item(0)
										.getNodeValue();
						i = i + 2;
					} else {

						strnode = element.getNodeName();
						strval = element.getChildNodes()
							.item(0) == null ? ""
									: element.getChildNodes()
										.item(0)
										.getNodeValue();
					}
					if (strval != null && !strval.equals("")) {
						if (strnode.indexOf(':') != -1) {
							strcmp = "~" + strnode.split(":")[1] + "~";
						} else {
							strcmp = "~" + strnode + "~";
						}
						if (securedXmlElements.indexOf(strcmp) != -1) {
							if (strcmp.equals("~Track2~") || strcmp.equals("~Track1~") || strcmp.equals("~Track1Data~")
									|| strcmp.equals("~Track2Data~") || strcmp.equals("~Password~") || strcmp.equals("~RespData~")
									|| strcmp.equals("~RespMsg~")) {
								strval = "************";
							} else if (strcmp.equals("~CVV2~")) {
								strval = "***";
							} else if (strcmp.equals("~SPNumber~") || strcmp.equals("~TargetCardNumber~")) {
								if(strval.length() > 4)
								    strval = strval.replace(strval.substring(4, strval.length() - 4), "****");
							}else {
								strval = strval.replace(strval.substring(4, strval.length() - 4), "****");
							}
							element.getChildNodes()
								.item(0)
								.setNodeValue(strval);
						}
					}
				} catch (Exception e) {
					logger.error("Exception occured while masking xml node name: {}", strnode, e);
				}
			}
		} catch (Exception e) {

			logger.error("Exception occured while masking the request/response: {}", e);
		}

		return doc;
	}

	public String maskSecuredData(String inXML) {
		String outXML = inXML;

		if (!Util.isEmpty(securedXmlElements)) {
			try {
				Document doc = parseAndMaskData(inXML);
				DOMSource domSource = new DOMSource(doc);
				TransformerFactory tf = TransformerFactory.newInstance();
				Transformer transformer = tf.newTransformer();
				java.io.StringWriter sw = new java.io.StringWriter();
				StreamResult sr = new StreamResult(sw);
				transformer.transform(domSource, sr);
				outXML = sw.toString();
			} catch (ParserConfigurationException | SAXException | IOException | TransformerException ex) {
				logger.error("Error occured while masking secure data: {}", ex);
			}
		}
		return outXML;
	}

}

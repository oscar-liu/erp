package com.whalegoods.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


/**
 * XML处理工具类
 * @author chencong
 * 2018年4月10日 上午10:03:11
 */
public class XmlUtil {

	/**
	 * 实体类转XML字符串
	 * @author chencong
	 * 2018年4月10日 下午4:59:46
	 */
	public static <T> String outputXml(T paramObj, Class<T> paramClass) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(paramClass);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		StringWriter stringWriter = new StringWriter();
		m.marshal(paramObj, stringWriter);
		String xmlString = stringWriter.toString();
		return xmlString;
	}
	
	/**
	 * XML字符串转MAP集合
	 * @author chencong
	 * 2018年4月10日 下午5:00:10
	 */
	public static Map<String, String> parseXml(String xmlData) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		InputStream inputStream = new ByteArrayInputStream(xmlData.getBytes());
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		Element root = document.getRootElement();
		@SuppressWarnings("unchecked")
		List<Element> elementList = root.elements();
		for (Element e : elementList) {
			map.put(e.getName(), e.getText());
		}
		inputStream.close();
		return map;
	}
}

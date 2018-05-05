package com.whalegoods.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.exception.SystemException;


/**
 * XML处理工具类
 * @author henrysun
 * 2018年4月10日 上午10:03:11
 */
public class XmlUtil{
	
	private static Logger logger = LoggerFactory.getLogger(XmlUtil.class);
	
	  @SuppressWarnings("rawtypes")
	public static String mapToXml(Map map) {    
	        StringBuffer sb = new StringBuffer();  
	        sb.append("<xml>");  
	        mapToXml(map, sb);
	        sb.append("</xml>");
	        return sb.toString();
	    }  
	  
	    @SuppressWarnings("rawtypes")
		private static void mapToXml(Map map, StringBuffer sb) {  
	        Set set = map.keySet();  
	        for (Iterator it = set.iterator(); it.hasNext();) {  
	            String key = (String) it.next();  
	            Object value = map.get(key);  
	            if (null == value)  
	                value = "";  
	            if (value.getClass().getName().equals("java.util.ArrayList")) {  
	                ArrayList list = (ArrayList) map.get(key);  
	                sb.append("<" + key + ">");  
	                for (int i = 0; i < list.size(); i++) {  
	                    HashMap hm = (HashMap) list.get(i);  
	                    mapToXml(hm, sb);  
	                }  
	                sb.append("</" + key + ">");  
	            } else {  
	                if (value instanceof HashMap) {  
	                    sb.append("<" + key + ">");  
	                    mapToXml((HashMap) value, sb);  
	                    sb.append("</" + key + ">");  
	                } else {  
	                    sb.append("<" + key + ">" + value + "</" + key + ">");  
	                }  
	            }  
	        }  
	    }  
	    
	      
	    /**  
	     * xml 转 Map  
	     * @param xml  
	     * @return  
	     * @throws DocumentException 
	     * @throws SystemException 
	     */  
	    public static Map<String,String> xmlToMap(String xml) throws SystemException 
	    {  
	        Map<String,String> map =new HashMap<>();
	        Document doc = null;  
	        try {
				doc = DocumentHelper.parseText(xml);
			} catch (DocumentException e) {
				logger.error("执行xmlToMap()方法失败："+xml+"原因："+e.getMessage());
				throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
			}  
	        if(doc ==null)  
	            return map;  
	        Element root = doc.getRootElement();  
	      for (@SuppressWarnings("rawtypes")
		Iterator iterator = root.elementIterator(); iterator.hasNext();) {  
	         Element e = (Element) iterator.next();  
	         map.put(e.getName(), e.getText());  
	      }  
	      return map;  
	    }  
	    
	    /**
	     * 解析POST XML请求流
	     * @param request
	     * @return
	     * @throws SystemException 
	     */
	    public static HashMap<String, String> parseXML(HttpServletRequest request) throws SystemException{
            // 将解析结果存储在HashMap中
            HashMap<String, String> map = new HashMap<String, String>();
            try {
                // 从request中取得输入流
                InputStream inputStream = request.getInputStream();
                // 读取输入流
                SAXReader reader = new SAXReader();
                Document document = reader.read(request.getInputStream());
                // 得到xml根元素
                Element root = document.getRootElement();
                recursiveParseXML(root,map);
                inputStream.close();
                inputStream = null;
			} catch (Exception e) {
				logger.error("执行parseXML方法错误："+e.getMessage());
				throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
			}
            return map;
        }
	    
	    private static void recursiveParseXML(Element root,HashMap<String, String> map){
            // 得到根元素的所有子节点
            @SuppressWarnings("unchecked")
			List<Element> elementList = root.elements();
            //判断有没有子元素列表
            if(elementList.size() == 0){
                map.put(root.getName(), root.getText());
            }else{
                //遍历
                for (Element e : elementList){
                    recursiveParseXML(e,map);
                }
            }
        }
}

package com.whalegoods.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.exception.SystemException;


/**
 * XML处理工具类
 * @author chencong
 * 2018年4月10日 上午10:03:11
 */
public class XmlUtil{
	
	
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
	     * @throws SystemException 
	     */  
	    public static Map<String,String> xmlToMap(String xml) throws SystemException  
	    {  
	        Map<String,String> map = new HashMap<String, String>();  
	        Document doc = null;  
	        try {  
	            doc = DocumentHelper.parseText(xml);  
	        } catch (DocumentException e) {  
	          throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
	        }  
	        if(doc ==null)  
	            return map;  
	        Element root = doc.getRootElement();  
	      for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {  
	         Element e = (Element) iterator.next();  
	         map.put(e.getName(), e.getText());  
	      }  
	      return map;  
	    }  
}

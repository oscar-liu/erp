package com.whalegoods.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

/**
 * IP处理工具类
 * @author chencong
 * 2018年4月10日 下午4:17:53
 */
public class IpUtil {

    public static String getIp(HttpServletRequest request){
        String ip=request.getHeader("x-forwarded-for");
        if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){
            ip=request.getHeader("Proxy-Client-IP");
        }
        if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){
            ip=request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){
            ip=request.getHeader("X-Real-IP");
        }
        if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){
            ip=request.getRemoteAddr();
        }
        return ip;
    }
    
    public static String getNativeIp() throws UnknownHostException{
    	return InetAddress.getLocalHost().getHostAddress().toString();
    }
}

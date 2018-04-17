package com.whalegoods.util;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import com.whalegoods.constant.ConstSysParamValue;

/**
 * 实现用于主机名验证的基接口
 * 在握手期间，如果 URL 的主机名和服务器的标识主机名不匹配，则验证机制可以回调此接口的实现程序来确定是否应该允许此连接
 * @author chencong
 * 2018年4月17日 下午5:45:48
 */
public class MyHostnameVerifier implements HostnameVerifier  {
	
    @Override  
    public boolean verify(String hostname, SSLSession session) {  
        if(ConstSysParamValue.WX_API.equals(hostname)){  
            return true;  
        } else {
            return false;  
        }  
    }  
}

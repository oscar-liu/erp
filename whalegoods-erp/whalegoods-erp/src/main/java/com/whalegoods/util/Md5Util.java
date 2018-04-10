package com.whalegoods.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
/**
 * MD5工具类
 * @author chencong
 *
 */
public class Md5Util {
	
	private static Logger logger = LoggerFactory.getLogger(Md5Util.class);
	
/*	public static String getSign(JSONObject reqJson,String secretkey){
		ResBodyData resBodyData=new ResBodyData(ApiStatusConst.SUCCESS,ApiStatusConst.getZhMsg(ApiStatusConst.SUCCESS));
		String serverSign=null;
		String[] arr = new String[reqJson.containsKey(SysParamsConst.SIGN)?(reqJson.size()- 1):reqJson.size()];
		int i=0;
		try {
			for (String key: reqJson.keySet()) {
				if (SysParamsConst.SIGN.equals(key))
					continue;
				arr[i] = key+ SysParamsConst.EQUALS_SYMBOL +reqJson.getString(key);
				i++;
			}
			Arrays.sort(arr);
			StringBuffer buffer = new StringBuffer();
			for (int k = 0; k < arr.length; k++) {
				buffer.append(arr[k]);
				buffer.append(SysParamsConst.CONNECTION_SYMBOL);
			}
			buffer.append(SysParamsConst.KEY_LAST);
			buffer.append(secretkey);
			serverSign=Md5Util.MD5EncryptBy32(buffer.toString()).toUpperCase();
			resBodyData.setData(serverSign);
		} catch (Exception e) {
			logger.error("exec getSign() error:{}",e.toString());
			resBodyData.setStatus(ApiStatusConst.GET_SIGN_EX);
			resBodyData.setMsg(ApiStatusConst.getZhMsg(ApiStatusConst.GET_SIGN_EX));
		}
		return resBodyData;
	}*/
	
	  public static String getMd5(String msg,String salt){
		    return new Md5Hash(msg,salt,4).toString();
	  }
	
}
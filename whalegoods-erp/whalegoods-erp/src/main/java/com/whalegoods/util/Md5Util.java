package com.whalegoods.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.constant.ConstSysParamName;
import com.whalegoods.exception.SystemException;
/**
 * MD5工具类
 * @author chencong
 *
 */
public class Md5Util {
	
	private static Logger logger = LoggerFactory.getLogger(Md5Util.class);
	
	/**
	 * 生成签名
	 * @author chencong
	 * 2018年4月10日 下午4:13:55
	 */
	public static String getSign(JSONObject json,String secretkey){
		String serverSign=null;
		String[] arr = new String[json.containsKey(ConstSysParamName.SIGN)?(json.size()- 1):json.size()];
		int i=0;
		try {
			for (String key: json.keySet()) {
				if (ConstSysParamName.SIGN.equals(key))
					continue;
				arr[i] = key+ ConstSysParamName.EQUALS_SYMBOL +json.getString(key);
				i++;
			}
			Arrays.sort(arr);
			StringBuffer buffer = new StringBuffer();
			for (int k = 0; k < arr.length; k++) {
				buffer.append(arr[k]);
				buffer.append(ConstSysParamName.CONNECTION_SYMBOL);
			}
			buffer.append(ConstSysParamName.KEY_LAST);
			buffer.append(secretkey);
			serverSign=getMd532(buffer.toString()).toUpperCase();
		} catch (Exception e) {
			logger.error("exec getSign() error:{}",e.toString());
		}
		return serverSign;
	}
	
	/**
	 * 生成MD5值
	 * @author chencong
	 * 2018年4月10日 下午4:14:09
	 * @throws SystemException 
	 */
	private static String getMd532(String values) throws SystemException {
		StringBuffer buf = new StringBuffer("");
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(values.getBytes());
			byte b[] = md.digest();
			int i;
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
		} catch (NoSuchAlgorithmException e) {
			logger.error("exec MD5EncryptBy32() error:{}",e.getMessage());
			throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
		}
		return buf.toString();
	}
	
	  public static String getMd5(String msg,String salt){
		    return new Md5Hash(msg,salt,4).toString();
	  }
	
}
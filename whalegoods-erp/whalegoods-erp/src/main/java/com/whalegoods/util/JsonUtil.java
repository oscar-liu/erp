package com.whalegoods.util;


import com.alibaba.fastjson.JSONObject;
import com.whalegoods.util.JsonUtil;



/**
 * 返回结果封装类
 * @author henry-sun
 *
 */
public class JsonUtil {
	
	  //默认成功
	  private boolean flag=true;
	  
	  private String msg;
	  
	 private JSONObject data;
	 
	 public JSONObject getData() {
		return data;
	}

	public void setData(JSONObject data) {
		this.data = data;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}


	public JsonUtil() {
	  }

	  public JsonUtil(boolean flag, String msg) {
	    this.flag = flag;
	    this.msg = msg;
	  }

	  public static JsonUtil error(String msg){
	    return new JsonUtil(false,msg);
	  }
	  
	  public  static JsonUtil sucess(String msg){
	    return new JsonUtil(true,msg);
	  }
}

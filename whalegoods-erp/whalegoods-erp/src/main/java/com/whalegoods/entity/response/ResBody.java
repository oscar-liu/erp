package com.whalegoods.entity.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * API返回结果封装实体类
 * @author henrysun
 * 2018年5月2日 上午11:22:23
 */
@Getter
@Setter
@ToString
public class ResBody  implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@JsonProperty("result_code")
    private Integer resultCode;
    
	@JsonProperty("result_msg")
    private String resultMsg;
    
    private Object data;
    
    public ResBody(){}
    
    public ResBody(Integer resultCode,String resultMsg){
    	this.resultCode=resultCode;
    	this.resultMsg=resultMsg;
    }
    
    public ResBody(Integer resultCode,String resultMsg,Object data){
    	this.resultCode=resultCode;
    	this.resultMsg=resultMsg;
    	this.data=data;
    }

}
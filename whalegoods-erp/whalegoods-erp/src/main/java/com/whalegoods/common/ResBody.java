package com.whalegoods.common;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * API返回结果封装实体类
 * @author henry-sun
 *
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
    
    public ResBody(Integer resultCode,String resultMsg){
    	this.resultCode=resultCode;
    	this.resultMsg=resultMsg;
    }

}
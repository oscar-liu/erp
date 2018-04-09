package com.whalegoods.entity.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * API请求映射实体类 基类
 * @author chencong
 * 2018年4月9日 下午5:15:12
 */
@Getter
@Setter
@ToString
public class reqBase implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("device_code_sup")
	private String deviceCodeSup;
	
	@JsonProperty("device_code_wg")
	private String deviceCodeWg;

}

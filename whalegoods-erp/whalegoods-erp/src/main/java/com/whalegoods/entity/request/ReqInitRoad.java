package com.whalegoods.entity.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 后台初始化货道操作请求映射实体类
 * @author henrysun
 * 2018年5月29日 上午10:59:40
 */
@Getter
@Setter
@ToString
public class ReqInitRoad implements Serializable{

	private static final long serialVersionUID = 1L;

	@NotNull(message="deviceId必填")
	private String deviceId;
	
	@NotNull(message="warningNum必填")
	private Byte warningNum;
	
	@NotNull(message="capacity必填")
	private Byte capacity;
	
}

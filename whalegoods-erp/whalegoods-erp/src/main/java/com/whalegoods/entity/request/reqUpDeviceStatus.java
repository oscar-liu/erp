package com.whalegoods.entity.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class reqUpDeviceStatus implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("device_code_sup")
	private String deviceCodeSup;
	
	@JsonProperty("device_code_wg")
	private String deviceCodeWg;
	
	@JsonProperty("device_status")
	private Integer deviceStatus;

}

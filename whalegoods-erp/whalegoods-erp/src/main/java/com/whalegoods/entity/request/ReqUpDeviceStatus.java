package com.whalegoods.entity.request;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 设备状态上报API（1服务中 2停用 3下线）请求映射实体类 
 * @author chencong
 * 2018年4月9日 下午5:16:17
 */
@Getter
@Setter
@ToString
public class ReqUpDeviceStatus extends ReqBase implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("device_status")
	@NotEmpty(message="设备状态不能为空")
	@Max(2)
	@Min(2)
	private Byte deviceStatus;

}

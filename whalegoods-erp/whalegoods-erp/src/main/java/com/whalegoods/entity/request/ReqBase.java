package com.whalegoods.entity.request;

import java.io.Serializable;

/*import org.hibernate.validator.constraints.Length;*/
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * API请求映射实体类 基类
 * @author henrysun
 * 2018年4月9日 上午10:28:32
 */
@Getter
@Setter
@ToString
public class ReqBase implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("device_code_sup")
	@NotEmpty(message="供应商设备号不能为空")
	private String device_code_sup;
	
	@JsonProperty("device_code_wg")
	@NotEmpty(message="系统设备号不能为空")
	private String device_code_wg;
	
	private String appid;
	
	private Long timestamp;
	
	private String sign;

}

package com.whalegoods.entity.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 根据当前设备货道号获取商品信息接口API  请求映射实体类 
 * @author chencong
 * 2018年4月9日 下午5:16:17
 */
@Getter
@Setter
@ToString
public class ReqGetInfoByPathCode extends ReqBase implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("path_code")
	@NotEmpty(message="货道编号不能为空")
	private String path_code;
	
	@JsonProperty("ctn")
	@NotNull(message="货道柜号不能为空")
	private String ctn;
	
	@JsonProperty("floor")
	@NotNull(message="货道层级不能为空")
	private String floor;

}

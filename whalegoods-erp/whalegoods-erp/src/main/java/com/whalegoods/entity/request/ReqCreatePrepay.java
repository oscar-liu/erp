package com.whalegoods.entity.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 生成预支付订单API 请求映射实体类 
 * @author chencong
 *
 */
@Getter
@Setter
@ToString
public class ReqCreatePrepay extends ReqBase{

	private static final long serialVersionUID = 1L;
	
	@NotNull(message="ctn必填")
	private Integer ctn;
	
	@NotNull(message="floor必填")
	private Integer floor;
	
	
	@JsonProperty("path_code")
	@NotNull(message="path_code必填")
	private Integer pathCode;
	
	@JsonProperty("view_time")
	private Long viewTime;
	
	@JsonProperty("sale_type")
	@NotNull(message="sale_type必填")
	@Max(2)
	@Min(1)
	private Byte saleType;
	
}

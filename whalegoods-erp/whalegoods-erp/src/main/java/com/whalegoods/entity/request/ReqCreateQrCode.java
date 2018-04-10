package com.whalegoods.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 生成商品支付二维码API 请求映射实体类 
 * @author chencong
 *
 */
@Getter
@Setter
@ToString
public class ReqCreateQrCode extends ReqBase{

	private static final long serialVersionUID = 1L;

	@JsonProperty("path_code")
	private String pathCode;
	
	@JsonProperty("view_time")
	private Integer viewTime;
	
	@JsonProperty("sale_type")
	private Integer saleType;
	
	@JsonProperty("pay_type")
	private Integer payType;
	
	@JsonProperty("goods_code")
	private Integer goodsCode;
	
}

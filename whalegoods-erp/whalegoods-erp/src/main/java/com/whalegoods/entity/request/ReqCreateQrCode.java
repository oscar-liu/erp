package com.whalegoods.entity.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

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
	@NotEmpty(message="path_code必填")
	private String pathCode;
	
	@JsonProperty("view_time")
	private Long viewTime;
	
	@JsonProperty("sale_type")
	@NotNull(message="sale_type必填")
	@Max(2)
	@Min(1)
	private Byte saleType;
	
	@JsonProperty("pay_type")
	@NotNull(message="pay_type必填")
	@Max(2)
	@Min(1)
	private Byte payType;
	
	@JsonProperty("goods_code")
	@NotEmpty(message="goods_code必填")
	private String goodsCode;
	
}

package com.whalegoods.entity.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class ResDeviceGoodsInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("goods_code")
	private String goodsCode;
	
	@JsonProperty("goods_name")
	private String goodsName;
	
	private Integer ctn;
	
	private String spec;
	
	@JsonProperty("goods_detail")
	private String goodsDetail;
	
	@JsonProperty("made_in")
	private String madeIn;
	
	@JsonProperty("market_price")
	private Double marketPrice;
	
	@JsonProperty("sale_price")
	private Double salePrice;
	
	@JsonProperty("path_code")
	private String pathCode;
	
	private Integer floor;
	
	@JsonProperty("pic_url")
	private String picUrl;
	
	private Integer stock;
}

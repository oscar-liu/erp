package com.whalegoods.entity.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 柜机APP获取促销商品信息API 映射实体类
 * @author henrysun
 * 2018年5月15日 上午10:42:06
 */
@Getter
@Setter
@ToString
public class ResGoodsAdsMiddle implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("goods_code")
	private String goodsCode;
	    
	@JsonProperty("pic_url")
	private String picUrl;
	
	@JsonProperty("start_time")
	private String startTime;
	
	@JsonProperty("end_time")
	private String endTime;
	
	//促销价格，goods_ads_middle表中的sale_price字段
	@JsonProperty("sale_price")
	private Double salePrice;
	
	@JsonProperty("stock")
	private Byte stock;
	
	//原价，device_road表中的sale_price字段
	@JsonProperty("market_price")
	private Double marketPrice;
	
}
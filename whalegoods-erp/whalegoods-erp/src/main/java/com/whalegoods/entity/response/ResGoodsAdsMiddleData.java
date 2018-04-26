package com.whalegoods.entity.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 中部促销活动栏商品列表API返回结果  data字段子集合（now和next）   实体类
 * @author chencong
 * 2018年4月9日 下午3:03:11
 */
@Getter
@Setter
@ToString
public class ResGoodsAdsMiddleData implements Serializable {
	
	private static final long serialVersionUID = 1L;
    
	@JsonProperty("goods_code")
	private String goodsCode;
	    
	@JsonProperty("pic_url")
	private String picUrl;
	
	@JsonProperty("sale_price")
	private Double salePrice;
	
	@JsonProperty("market_price")
	private Double marketPrice;
	
	@JsonProperty("stock")
	private Byte stock;
	
}
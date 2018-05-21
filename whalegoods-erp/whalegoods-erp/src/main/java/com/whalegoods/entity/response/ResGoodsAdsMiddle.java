package com.whalegoods.entity.response;

import java.io.Serializable;

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
	
	private String goodsCode;
	    
	private String picUrl;
	
	private Double salePrice;
	
	private Integer stock;
	
	private Double marketPrice;
}
package com.whalegoods.entity.response;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class ResGoodsAdsMiddleJson implements Serializable {
	
	private static final long serialVersionUID = 1L;
    
	@JSONField(name="goods_name")
	private String goodsName;
	    
	@JSONField(name="pic_url")
	private String picUrl;
	
	@JSONField(name="sale_price")
	private Double salePrice;
	
}
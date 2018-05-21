package com.whalegoods.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 广告表goods_ads_top实体类
 * @author henry-sun
 *
 */
@Getter
@Setter
@ToString
public class GoodsAdsTop extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
    
    private String deviceId;
    
    private Byte actionType;
    
    private String goodsCode;
    
    private String goodsName;
    
    private String bigPicUrl;
    
    private String tinyPicUrl;
    
    private Integer stock;
    
    private String shortName;
    
    private Double salePrice;

}
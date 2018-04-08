package com.whalegoods.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 顶部促销表goods_ads_top实体类
 * @author henry-sun
 *
 */
@Getter
@Setter
@ToString
public class GoodsAdsTop extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
    
    private String deviceRoadId;
    
    private String bigPicUrl;
    
    private String tinyPicUrl;

}
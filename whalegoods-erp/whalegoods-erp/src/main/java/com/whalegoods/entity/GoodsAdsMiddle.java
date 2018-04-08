package com.whalegoods.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 中部促销表goods_ads_middle实体类
 * @author henry-sun
 *
 */
@Getter
@Setter
@ToString
public class GoodsAdsMiddle extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
    
    private String deviceRoadId;
    
    private Date startTime;
    
    private Date endTime;
    
    private Byte type;

}
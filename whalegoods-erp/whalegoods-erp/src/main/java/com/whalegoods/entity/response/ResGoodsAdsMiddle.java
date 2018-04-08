package com.whalegoods.entity.response;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class ResGoodsAdsMiddle implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String goodsName;
	    
	private Date startTime;
	    
    private Date endTime;
	    
	private String picUrl;
	
	private Byte type;
	
	private Double salePrice;
}
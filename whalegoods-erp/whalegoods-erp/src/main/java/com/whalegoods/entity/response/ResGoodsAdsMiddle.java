package com.whalegoods.entity.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Mybatis根据设备编号查询中部促销商品信息   sql_id:selectByDeviceId  映射实体类
 * @author chencong
 * 2018年4月9日 下午3:00:33
 */
@Getter
@Setter
@ToString
public class ResGoodsAdsMiddle implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String goodsCode;
	    
	private String picUrl;
	
	private Byte type;
	
	private Double salePrice;
	
	private Byte stock;
	
	private Double marketPrice;
}
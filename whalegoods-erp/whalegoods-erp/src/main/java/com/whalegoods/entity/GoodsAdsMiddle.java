package com.whalegoods.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

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
    
    private String goodsCode;
    
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    
    private String startHms;
    
    private String endHms;
    
    private Byte type;
    
    private String deviceId;
   
    //原价，device_road表中的sale_price字段
    private Double marketPrice;
    
    private String picUrl;
    
    private String goodsName;
    
    //促销价格，goods_ads_middle表中的sale_price字段
    private Double salePrice;
    
    private Byte stock;
    
    private String shortName;
    
    //用于在ERP列表中组合显示
    private String timeRange;
    
    //layui时间控件映射字段
    private String hmsRange;
    
  //layui日期控件映射字段
    private String dateRange;
}
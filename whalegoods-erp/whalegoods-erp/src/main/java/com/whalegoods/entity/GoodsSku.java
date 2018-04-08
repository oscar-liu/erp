package com.whalegoods.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 商品基础SKU表goods_sku实体类
 * @author henry-sun
 *
 */
@Getter
@Setter
@ToString
public class GoodsSku extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

    private String goodsCode;
    
    private String goodsname;
    
    private String spec;
    
    private Integer boxNum;
    
    private Double oneCost;
    
    private Double boxCost;
    
    private Double marketPrice;
    
    private Double profit;
    
    private String picUrl;
    
    private String suppName;
    
    private String className;
    
    private Date productDate;
    
    private Integer shelfLife;
    
    private String madeIn;
    
    private String goodsDetail;

}
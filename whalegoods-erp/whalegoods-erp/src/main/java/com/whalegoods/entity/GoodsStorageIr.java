package com.whalegoods.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 内部购买登记表goods_storage_ir实体类
 * @author henrysun
 * 2018年9月28日 下午4:16:27
 */
@Getter
@Setter
@ToString
public class GoodsStorageIr extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date saleDate;
	
    private String goodsSkuId;
    
    private String goodsCode;
    
    private String goodsName;
    
    private Integer saleCount;
    
    private Double salePrice;
    
    private Double totalSalePrice;
  
    //入库批次名称，由多个字段组合而成
    private String goodsStorageInName;
    
    private String goodsStorageInId;
    
    private String startSaleDate;
    
    private String endSaleDate;
    
    private String timeRange;
}
package com.whalegoods.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 商品报损表goods_storage_rd实体类
 * @author henrysun
 * 2018年9月18日 下午6:32:51
 */
@Getter
@Setter
@ToString
public class GoodsStorageRtw extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date rtwDay;
    
    private Integer rtwNum;
    
    private String deviceId;
    
    private String shortName;
	
    private String goodsSkuId;
    
    private String goodsStorageInId;
    
    private String goodsCode;
    
    private String goodsName;
    
    //入库批次名称，由多个字段组合而成
    private String goodsStorageInName;
   
}
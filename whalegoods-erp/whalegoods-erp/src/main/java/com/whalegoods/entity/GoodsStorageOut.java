package com.whalegoods.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 仓库出库表goods_storage_out实体类
 * @author henrysun
 * 2018年8月28日 下午2:19:08
 */
@Getter
@Setter
@ToString
public class GoodsStorageOut extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    private String goodsSkuId;
    
    private String goodsStorageInId;
    
    private String goodsCode;
    
    private String goodsName;
    
    private Integer applyNum;
    
    private String applyBy;
    
    private String userName;
    
    private String deviceId;
	
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date applyDate;
    
    private String shortName;
    
    private String startApplyDate;
    
    private String endApplyDate;
    
    private String timeRange;
    
    //进货批次名称，由多个字段组合而成
    private String goodsStorageInName;
   
}
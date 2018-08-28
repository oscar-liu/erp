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
    
    private Integer applyNum;
    
    private String applyBy;
    
    private String deviceId;
	
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date applyDate;
   
}
package com.whalegoods.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 仓库入库表goods_storage_in实体类
 * @author henrysun
 * 2018年8月28日 下午2:13:19
 */
@Getter
@Setter
@ToString
public class GoodsStorageIn extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date inDate;
	
    private String goodsSkuId;
    
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date productDate;
    
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date expiringDate;
    
    private Double costPrice;
    
    private Double marketPrice;
  
    private Integer inCount;
    
    private String goodsCode;
    
    private String goodsName;
  
}
package com.whalegoods.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 仓库库存表goods_storage实体类
 * @author henrysun
 * 2018年8月28日 下午2:13:19
 */
@Getter
@Setter
@ToString
public class GoodsStorage extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    private String goodsSkuId;
    
    private String goodsCode;
    
    private String goodsName;
    
    private Integer currCount;
    
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date expiringDate;
    
}
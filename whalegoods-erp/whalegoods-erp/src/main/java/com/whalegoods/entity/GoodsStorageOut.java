package com.whalegoods.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
	
	@Excel(name = "出库单号",orderNum = "2")
	private String outId;
	
    private String goodsSkuId;
    
    private String goodsStorageInId;
    
    @Excel(name = "商品编号",orderNum = "5")
    private String goodsCode;
    
    @Excel(name = "商品名称",orderNum = "4")
    private String goodsName;
    
    @Excel(name = "数量（个）",orderNum = "6")
    private Integer applyNum;
    
    private String applyBy;
    
    private String userName;
    
    private String deviceId;
	
    @Excel(name = "出库日期",orderNum = "1", exportFormat = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date applyDate;
    
    @Excel(name = "点位",orderNum = "3")
    private String shortName;
    
    private String startApplyDate;
    
    private String endApplyDate;
    
    private String timeRange;
    
    //入库批次名称，由多个字段组合而成
    private String goodsStorageInName;
    
    private String goodsStorageOutName;
   
}
package com.whalegoods.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
	
    @Excel(name = "入库日期",orderNum = "1", exportFormat = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date inDate;
	
	@Excel(name = "入库单号",orderNum = "2")
    private String inId;
	
    private String goodsSkuId;
    
    @Excel(name = "生产日期",orderNum = "5", exportFormat = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date productDate;
    
    @Excel(name = "到期日期",orderNum = "6", exportFormat = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date expiringDate;
    
    @Excel(name = "成本价",orderNum = "7")
    private Double costPrice;
    
    @Excel(name = "建议零售价",orderNum = "8")
    private Double marketPrice;
  
    @Excel(name = "入库数量（个）",orderNum = "9")
    private Integer inCount;
    
    @Excel(name = "当前批次库存（个）",orderNum = "10")
    private Integer currCount;
    
    @Excel(name = "商品编号",orderNum = "4")
    private String goodsCode;
    
    @Excel(name = "商品名称",orderNum = "3")
    private String goodsName;
    
    @Excel(name = "库位",orderNum = "11")
    private String locationName;
    
    private String goodsStorageLocationId;
    
    private String startExpiringDate;
    
    private String endExpiringDate;
    
    private String timeRange;
    
    //入库批次名称，由多个字段组合而成
    private String goodsStorageInName;
    
    @Excel(name = "备注",orderNum = "12")
    private  String remark;
  
}
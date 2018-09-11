package com.whalegoods.entity;

import java.io.Serializable;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 总销量和总销售额实体类
 * @author henrysun
 * 2018年6月27日 下午3:45:35
 */
@Getter
@Setter
@ToString
public class ReportTotalCountAndAmount  extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Excel(name = "销量",orderNum = "8")
	private Integer salesCount;
	
	@Excel(name = "销售额",orderNum = "7")
    private Double salesAmount;
	
	@Excel(name = "成本",orderNum = "9")
    private Double costsAmount;
	
	@Excel(name = "毛利",orderNum = "10")
    private Double profit;
	
	@Excel(name = "成本利润率",orderNum = "11")
    private Double costProfit;
	
	@Excel(name = "销售利润率",orderNum = "12")
    private Double salesProfit;

}
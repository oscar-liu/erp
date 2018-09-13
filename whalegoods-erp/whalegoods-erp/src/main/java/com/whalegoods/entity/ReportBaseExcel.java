package com.whalegoods.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 基本统计报表report_base实体类
 * @author henrysun
 * 2018年6月21日 上午11:31:33
 */
@Getter
@Setter
@ToString
public class ReportBaseExcel extends ReportTotalCountAndAmount {
	
	private static final long serialVersionUID = 1L;
	
	@Excel(name = "设备编号",orderNum = "2")
	private String deviceCodeSup;
    
    @Excel(name = "商品编号",orderNum = "3")
    private String goodsCode;
    
    @Excel(name = "商品名称",orderNum = "4")
    private String goodsName;
    
    @Excel(name = "点位",orderNum = "1")
    private String shortName;
    
    private String dayRange;
    
    private String startOrderDay;
    
    private String endOrderDay;

}
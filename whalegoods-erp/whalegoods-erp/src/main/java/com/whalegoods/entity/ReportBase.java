package com.whalegoods.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

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
public class ReportBase extends ReportTotalCountAndAmount {
	
	private static final long serialVersionUID = 1L;
	
	@Excel(name = "设备编号（供应商）",orderNum = "3")
	private String deviceCodeSup;
	
	@Excel(name = "设备编号（鲸品）",orderNum = "4")
	private String deviceCodeWg;
	
    private String deviceId;
    
    @Excel(name = "商品编号",orderNum = "5")
    private String goodsCode;
    
    @Excel(name = "订单日期",orderNum = "1", exportFormat = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date orderDay;
    
    @Excel(name = "商品名称",orderNum = "6")
    private String goodsName;
    
    @Excel(name = "点位",orderNum = "2")
    private String shortName;
    
    private String dayRange;
    
    private String startOrderDay;
    
    private String endOrderDay;

}
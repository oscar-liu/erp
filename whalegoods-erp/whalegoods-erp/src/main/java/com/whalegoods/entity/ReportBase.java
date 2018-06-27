package com.whalegoods.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	
    private String deviceId;
    
    private String goodsCode;
    
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date orderDay;
    
    private String goodsName;
    
    private String shortName;
    
    private String dayRange;
    
    private String startOrderDay;
    
    private String endOrderDay;

}
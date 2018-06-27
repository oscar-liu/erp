package com.whalegoods.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 统计报表（按商品）report_by_goods实体类
 * @author henrysun
 * 2018年6月25日 下午2:00:55
 */
@Getter
@Setter
@ToString
public class ReportByGoods extends ReportTotalCountAndAmount {
	
	private static final long serialVersionUID = 1L;
	
    private String goodsCode;
    
    private String goodsName;
    
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date orderDay;
    
    private String dayRange;
    
    private String startOrderDay;
    
    private String endOrderDay;

}
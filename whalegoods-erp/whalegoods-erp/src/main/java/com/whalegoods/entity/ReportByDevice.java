package com.whalegoods.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 统计报表（按设备）report_by_device实体类
 * @author henrysun
 * 2018年6月21日 上午11:31:33
 */
@Getter
@Setter
@ToString
public class ReportByDevice extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    private String deviceId;
    
    private String shortName;

    private Integer salesCount;
	
    private Double salesAmount;
    
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date orderDay;
    
    private String dayRange;
    
    private String startOrderDay;
    
    private String endOrderDay;

}
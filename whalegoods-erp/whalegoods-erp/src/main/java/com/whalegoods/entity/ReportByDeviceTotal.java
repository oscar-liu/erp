package com.whalegoods.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 统计报表（按设备）总额统计表report_by_device_total实体类
 * @author henrysun
 * 2018年6月21日 上午11:31:33
 */
@Getter
@Setter
@ToString
public class ReportByDeviceTotal extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

    private Integer salesCount;
	
    private Double salesAmount;
    
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date orderDay;

}
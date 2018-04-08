package com.whalegoods.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 设备管理表device实体类
 * @author henry-sun
 *
 */
@Getter
@Setter
@ToString
public class Device extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

    private String deviceIdJp;
    
    private String deviceIdSupp;
    
    private String location;
    
    private Byte deviceStatus;
    
    private Byte runStatus;
    
    private Integer upTime;
    
    private String signCode;
    
    private String deviceSuppId;
    
    private String appVersionId;
    
    private Byte lockStatus;

}
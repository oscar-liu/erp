package com.whalegoods.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 设备供应商表device_supp实体类
 * @author henry-sun
 *
 */
@Getter
@Setter
@ToString
public class DeviceSupp extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

    private String suppName;
    
    private String linkName;
    
    private String linkWay;

}
package com.whalegoods.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 设备型号表device_model实体类
 * @author henry-sun
 *
 */
@Getter
@Setter
@ToString
public class DeviceModel extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

    private String modelName;

}
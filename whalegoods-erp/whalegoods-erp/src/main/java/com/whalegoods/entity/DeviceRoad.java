package com.whalegoods.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 货道信息表device_road实体类
 * @author henry-sun
 *
 */
@Getter
@Setter
@ToString
public class DeviceRoad extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    private String deviceIdJp;
    
    private String deviceIdSupp;
    
    private Byte ctn;
    
    private String pathCode;
    
    private Byte floor;
    
    private String goodsSkuId;
    
    private Double salePrice;
    
    private Byte capacity;
    
    private Byte stock;
    
    private Byte warningNum;
    
    private Byte warningSwitch;
    
    private Byte lackLevel;
    
    private String monitorThemeId;

}
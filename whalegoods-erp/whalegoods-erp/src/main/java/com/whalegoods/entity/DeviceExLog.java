package com.whalegoods.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 设备异常上报日志devcie_exlog实体类
 * @author henrysun
 * 2018年6月29日 下午2:44:25
 */
@Getter
@Setter
@ToString
public class DeviceExLog extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

    private String orderId;
    
    private String errorMessage;
    
    private String deviceId;
    
    private String shortName;
    
    private String goodsName;
    
    private String fileUrl;

}
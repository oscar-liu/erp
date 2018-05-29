package com.whalegoods.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 型号标准表device_model_standard实体类
 * @author henry-sun
 *
 */
@Getter
@Setter
@ToString
public class DeviceModelStandard extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

    private String deviceModelId;
    
    private String modelName;
    
    private Byte ctn;
    
    private Byte floor;
    
    private Byte pathNum;

	public DeviceModelStandard() {
	}
    
}
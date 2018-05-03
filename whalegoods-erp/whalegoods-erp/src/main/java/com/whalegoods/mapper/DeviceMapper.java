package com.whalegoods.mapper;


import com.whalegoods.entity.Device;

import java.util.Map;


public interface DeviceMapper extends BaseMapper<Device,String> {

	/**
	 * @author henrysun
	 * 2018年4月28日 下午2:46:35
	 */
    Map<String,Object> getApk(Map<String, Object> mapCdt);

    /**
     * 查询设备开启/启用状态
     * @author henrysun
     * 2018年5月2日 上午11:28:12
     */
    Integer getDeviceStatus(Map<String, Object> mapCdt);

}
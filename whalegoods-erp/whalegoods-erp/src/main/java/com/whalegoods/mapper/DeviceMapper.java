package com.whalegoods.mapper;


import com.whalegoods.entity.Device;

import java.util.Map;


public interface DeviceMapper extends BaseMapper<Device,String> {

    /**
     * 查询设备开启/启用状态
     * @author henrysun
     * 2018年5月2日 上午11:28:12
     */
    Integer getDeviceStatus(Map<String, Object> mapCdt);

}
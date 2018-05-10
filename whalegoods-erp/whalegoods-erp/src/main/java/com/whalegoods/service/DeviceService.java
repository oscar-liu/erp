package com.whalegoods.service;


import java.util.Map;

import com.whalegoods.entity.Device;


public interface DeviceService extends BaseService<Device,String> {
  
  /**
   * 查询设备运营状态，运营状态（1正在运行 2停止运行）
   * @author chencong
   * 2018年4月9日 上午10:59:49
   */
  Integer getDeviceStatus(Map<String,Object> mapCdt);

}

package com.whalegoods.service;


import java.util.Map;

import com.whalegoods.entity.Device;
import com.whalegoods.entity.response.ResBody;


public interface DeviceService extends BaseService<Device,String> {
  
  /**
   * 查询设备运营状态，运营状态（1正在运行 2停止运行）
   * @author chencong
   * 2018年4月9日 上午10:59:49
   */
  int getOperateStatus(Map<String,Object> condition);
  
  /**
   * 查询最新柜机APK包版本号和下载链接
   * @author chencong
   * 2018年4月9日 下午3:16:05
   */
  ResBody getApk(Map<String,Object> condition);

}

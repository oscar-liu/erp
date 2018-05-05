package com.whalegoods.controller;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.Device;
import com.whalegoods.entity.request.ReqBase;
import com.whalegoods.entity.request.ReqUpDeviceStatus;
import com.whalegoods.entity.request.ReqUploadLog;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.DeviceService;
import com.whalegoods.util.FileUtil;

/**
 * 设备管理API
 * @author henry-sun
 *
 */
@RestController
@RequestMapping(value = "/v1/device")
public class V1DeviceController {

  @Autowired
  DeviceService deviceService;
  
  @Autowired
  FileUtil fileUtil;
  
  /**
   * 设备状态上报接口（1服务中 2停用 3下线）
   * @author chencong
   * 2018年4月9日 上午11:06:19
 * @throws SystemException 
   */
  @PostMapping(value="/updateDeviceStatus")
  ResBody updateDeviceStatus(@RequestBody ReqUpDeviceStatus model) throws SystemException {
	  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
	  Device device=new Device();
	  device.setDeviceIdJp(model.getDevice_code_wg());
	  device.setDeviceIdSupp(model.getDevice_code_sup());
	  device.setDeviceStatus(model.getDeviceStatus());
	  deviceService.updateByObjCdt(device);	  
	  return resBody;
	}
  
  /**
   * 查询运营状态（1正在运行 2停止运行）
   * @author chencong
   * 2018年4月9日 上午11:05:57
   */
  @GetMapping(value="/getOperateStatus")
  ResBody getOperateStatus(@Valid ReqBase model) {
	  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
	  Map<String,Object> mapCdt=new HashMap<>();
	  mapCdt.put("deviceIdJp",model.getDevice_code_wg());
	  mapCdt.put("deviceIdSupp",model.getDevice_code_sup());
	  int status=deviceService.getDeviceStatus(mapCdt);
	  Map<String,Object> mapData=new HashMap<>();
	  mapData.put("operate_status",status);
	  resBody.setData(mapData);
	  return resBody;
	}
  
  /**
   * 客户端升级接口
   * @author chencong
   * 2018年4月9日 上午11:05:57
   */
  @GetMapping(value="/updateClient")
  ResBody updateClient(@Valid ReqBase model) {
	  Map<String,Object> condition=new HashMap<>();
	  condition.put("deviceIdJp",model.getDevice_code_wg());
	  condition.put("deviceIdSupp",model.getDevice_code_sup());
	  return deviceService.getApk(condition);
	}
  
  /**
   * 上传异常文件
   * @author chencong
   * 2018年4月23日 上午10:44:47
 * @throws SystemException 
 * @throws FileNotFoundException 
   */
  @PostMapping(value="/uploadExLog")
  public ResBody uploadExLog(@Valid ReqUploadLog model,HttpServletRequest request,HttpSession session) throws SystemException {
	  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
	  String childFolder="ex_log";
	  String newFileName=childFolder+"_"+model.getOrder();
	  fileUtil.uploadFile(request,childFolder,newFileName);
	  return resBody;
	}
  
 
  
}

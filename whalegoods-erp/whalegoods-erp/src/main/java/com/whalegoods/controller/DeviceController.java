package com.whalegoods.controller;

import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.constant.ConstSysParamName;
import com.whalegoods.entity.Device;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.service.DeviceService;
import com.whalegoods.util.ReType;
import com.whalegoods.util.ShiroUtil;
import com.whalegoods.util.StringUtil;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 后台系统设备管理接口
 * @author henrysun
 * 2018年4月25日 下午7:59:49
 */
@Controller
@RequestMapping(value = "/device")
public class DeviceController {
	
	  @Autowired
	  DeviceService deviceService;

	  /**
	   * 跳转到设备列表页面
	   * @author henrysun
	   * 2018年4月26日 下午3:29:12
	   */
	  @GetMapping(value = "showDevice")
	  @RequiresPermissions("device:show")
	  public String showDevice(Model model) {
	    return "/device/deviceList";
	  }

	  /**
	   * 查询设备列表接口
	   * @author henrysun
	   * 2018年4月26日 下午3:29:23
	   */
	  @GetMapping(value = "showDeviceList")
	  @ResponseBody
	  @RequiresPermissions("device:show")
	  public ReType showDeviceList(Model model, Device device, String page, String limit) {
	    return deviceService.selectByPage(device,Integer.valueOf(page),Integer.valueOf(limit));
	  }

	  /**
	   * 跳转到添加设备页面
	   * @author henrysun
	   * 2018年4月26日 下午3:30:10
	   */
	  @GetMapping(value = "showAddDevice")
	  public String showAddDevice(Model model) {
	    return "/device/add-device";
	  }

	  /**
	   * 添加设备接口
	   * @author henrysun
	   * 2018年4月26日 下午3:30:25
	   */
	  @PostMapping(value = "addDevice")
	  @ResponseBody
	  public ResBody addUser(Device device) {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		device.setId(StringUtil.getUUID());
		device.setDeviceIdJp(ConstSysParamName.DEVICE_PREFIX+StringUtil.getNumberRadom());
    	String currentUserId=ShiroUtil.getCurrentUserId();
    	device.setCreateBy(currentUserId);
    	device.setUpdateBy(currentUserId);
		deviceService.insert(device);
	    return resBody;
	  }

	  /**
	   * 跳转到更新设备页面
	   * @author henrysun
	   * 2018年4月26日 下午3:30:35
	   */
	  @GetMapping(value = "showUpdateDevice")
	  public String showUpdateDevice(String id, Model model){
		Device device=deviceService.selectById(id);
		model.addAttribute("device", device);
	    return "/device/update-device";
	  }


	  /**
	   * 更新设备接口
	   * @author henrysun
	   * 2018年4月26日 下午3:30:49
	   */
	  @PostMapping(value = "updateDevice")
	  @ResponseBody
	  public ResBody updateDevice(@RequestBody Device device) {
		  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		  deviceService.updateByObjCdt(device);
		  return resBody;
	  }

	  /**
	   * 删除设备接口
	   * @author henrysun
	   * 2018年4月26日 下午3:32:39
	   */
	  @PostMapping(value = "/delDevice")
	  @ResponseBody
	  @RequiresPermissions("device:del")
	  public ResBody delDevice(@RequestParam String id) {
	    ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
	    deviceService.deleteById(id);
	   return resBody;
	  }

	  
	  /**
	   * 更新设备运行状态
	   * @author henrysun
	   * 2018年5月6日 上午10:30:07
	   */
	  @PostMapping(value = "updateDeviceStatus")
	  @ResponseBody
	  public ResBody updateDeviceStatus(@RequestParam String id,@RequestParam(name="device_status") Boolean isCheck) {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		Device device=new Device();
		device.setId(id);
		//1服务中 2停用
		if(isCheck){
			device.setDeviceStatus((byte) 1);
		}
		else{
			device.setDeviceStatus((byte) 2);
		}
		deviceService.updateByObjCdt(device);
	    return resBody;
	  }
	  
	  /**
	   * 更新设备库存锁定状态
	   * @author henrysun
	   * 2018年5月6日 上午10:30:07
	   */
	  @PostMapping(value = "updateLockStatus")
	  @ResponseBody
	  public ResBody updateLockStatus(@RequestParam String id,@RequestParam(name="lock_status") Boolean isCheck) {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		Device device=new Device();
		device.setId(id);
		//1已锁定 2未锁定
		if(isCheck){
			device.setLockStatus((byte) 2);
		}
		else{
			device.setLockStatus((byte) 1);
		}
		deviceService.updateByObjCdt(device);
	    return resBody;
	  }

}

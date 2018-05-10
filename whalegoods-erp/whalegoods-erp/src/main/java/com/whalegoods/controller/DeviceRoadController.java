package com.whalegoods.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.Device;
import com.whalegoods.entity.DeviceRoad;
import com.whalegoods.entity.GoodsAdsTop;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.exception.BizApiException;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.DeviceRoadService;
import com.whalegoods.service.DeviceService;
import com.whalegoods.service.GoodsAdsMiddleService;
import com.whalegoods.service.GoodsAdsTopService;
import com.whalegoods.util.FileUtil;
import com.whalegoods.util.JsonUtil;
import com.whalegoods.util.ReType;
import com.whalegoods.util.ShiroUtil;
import com.whalegoods.util.StringUtil;


/**
 * 设备相关接口
 * @author henrysun
 * 2018年5月8日 上午9:53:55
 */
@Controller
@RequestMapping(value = "/road")
public class DeviceRoadController  {
	
	  @Autowired
	  DeviceRoadService deviceRoadService; 
	  
	  @Autowired
	  DeviceService deviceService; 
	  
	  @Autowired
	  GoodsAdsTopService goodsAdsTopService; 
	  
	  @Autowired
	  GoodsAdsMiddleService goodsAdsMiddleService; 
	  
	  @Autowired
	  FileUtil fileUtil;

	  /**
	   * 跳转到货道列表页面
	   * @author henrysun
	   * 2018年5月7日 上午10:12:39
	   */
	  @GetMapping(value = "showRoad")
	  @RequiresPermissions("device:road:list")
	  public String showRoad(Model model) {
	    return "/device/road/roadList";
	  }

	  /**
	   * 查询货道列表接口
	   * @author henrysun
	   * 2018年5月7日 上午10:12:34
	   */
	  @GetMapping(value = "showRoadList")
	  @ResponseBody
	  @RequiresPermissions("device:road:list")
	  public ReType showRoadList(Model model, DeviceRoad deviceRoad , String page, String limit) {
		  return deviceRoadService.selectByPage(deviceRoad,Integer.valueOf(page),Integer.valueOf(limit));
	  }
	  
	  /**
	   * 跳转到添加货道信息页面
	   * @author henrysun
	   * 2018年5月8日 上午10:09:50
	   */
	  @GetMapping(value = "showAddRoad")
	  public String showAddRoad(Model model) {
	    return "/device/road/add-road";
	  }

	  /**
	   * 添加货道接口
	   * @author henrysun
	   * 2018年5月7日 上午11:44:51
	   */
	  @PostMapping(value = "addRoad")
	  @ResponseBody
	  public ResBody addRoad(@RequestBody DeviceRoad deviceRoad) {
		  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		  //查询设备是否存在
		  Device objCdt=new Device();
		  objCdt.setDeviceIdJp(deviceRoad.getDeviceIdJp());
		  objCdt.setDeviceIdSupp(deviceRoad.getDeviceIdSupp());
		  if(!(deviceService.selectCountByObjCdt(objCdt)>0))
		  {
			  throw new BizApiException(ConstApiResCode.DEVICE_NOT_EXIST);
		  }
		  //查询货道是否已存在
		  Map<String,Object> mapCdt=new HashMap<>();
		  mapCdt.put("deviceIdJp",deviceRoad.getDeviceIdJp());
		  mapCdt.put("ctn",deviceRoad.getCtn());
		  mapCdt.put("floor",deviceRoad.getFloor());
		  mapCdt.put("pathCode",deviceRoad.getPathCode());
		  if(!(deviceRoadService.selectCountByMapCdt(mapCdt)>0)){
			  throw new BizApiException(ConstApiResCode.PATH_EXIST);
		  }
		  deviceRoad.setId(StringUtil.getUUID());
		  deviceRoad.setCreateBy(ShiroUtil.getCurrentUserId());
		  deviceRoad.setUpdateBy(ShiroUtil.getCurrentUserId());
		  deviceRoadService.insert(deviceRoad);
		  return resBody;
	  }

	  /**
	   * 跳转到更新货道信息页面
	   * @author henrysun
	   * 2018年5月7日 上午11:54:54
	   */
	  @GetMapping(value = "showUpdateRoad")
	  public String showUpdateRoad(@RequestParam String id, Model model) {
		DeviceRoad deviceRoad= deviceRoadService.selectById(id);
		model.addAttribute("road",deviceRoad);
	    return "/device/road/update-road";
	  }


	  /**
	   * 更新货道信息接口
	   * @author henrysun
	   * 2018年4月26日 下午3:30:49
	   */
	  @PostMapping(value = "updateRoad")
	  @ResponseBody
	  public ResBody updateRoad(@RequestBody DeviceRoad deviceRoad) {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		deviceRoad.setUpdateBy(ShiroUtil.getCurrentUserId());
		deviceRoadService.updateByObjCdt(deviceRoad);
		return resBody;
	  }

	  /**
	   * 删除货道接口
	   * @author henrysun
	   * 2018年4月26日 下午3:32:39
	   */
	  @PostMapping(value = "delRoad")
	  @ResponseBody
	  @RequiresPermissions("device:road:del")
	  public ResBody delRoad(@RequestParam String id) {
       ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
       deviceRoadService.deleteById(id);
	   return resBody;
	  }
	  
	  /**
	   * 跳转到设置中部促销活动页面
	   * @author henrysun
	   * 2018年5月8日 上午10:09:50
	   */
	  @GetMapping(value = "showAddAdsMiddle")
	  public String showAddAdsMiddle(Model model,@RequestParam String middleData) {
		  model.addAttribute("deviceRoads", middleData);
	    return "/device/road/add-adsmiddle";
	  }

	  /**
	   * 添加中部促销活动接口
	   * @author henrysun
	   * 2018年5月7日 上午11:44:51
	   */
	  @SuppressWarnings("rawtypes")
	  @PostMapping(value = "addAdsMiddle")
	  @ResponseBody
	  public ResBody addAdsMiddle(@RequestBody String json) {
		  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));	  
		  List<Map> mapList=JSON.parseArray(json,Map.class);
		  for (Map map : mapList) {
			//添加操作
		}
		  return resBody;
	  }
	  
	  /**
	   * 跳转到设置顶部广告页面
	   * @author henrysun
	   * 2018年5月8日 上午10:09:50
	   */
	  @GetMapping(value = "showAddAdsTop")
	  public String showAddAdsTop(Model model,@RequestBody GoodsAdsTop adsTop ) {
		  model.addAttribute("topData", adsTop);
	    return "/device/road/add-adstop";
	  }

	  /**
	   * 添加顶部广告接口
	   * @author henrysun
	   * 2018年5月7日 上午11:44:51
	   */
	  @PostMapping(value = "addAdsTop")
	  @ResponseBody
	  public ResBody addAdsTop(@RequestBody GoodsAdsTop adsTop) {
		  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		  adsTop.setId(StringUtil.getUUID());
		  adsTop.setCreateBy(ShiroUtil.getCurrentUserId());
		  adsTop.setUpdateBy(ShiroUtil.getCurrentUserId());
		  goodsAdsTopService.insert(adsTop);
		  return resBody;
	  }
	  
	  /**
	   * 上传顶部广告大图接口
	   * @author henrysun
	   * 2018年5月7日 上午11:42:14
	   */
	  @PostMapping(value = "uploadBigPic")
	  @ResponseBody
	  public JsonUtil uploadBigPic(HttpServletRequest request,HttpSession session) {
		  JsonUtil jsonUtil=JsonUtil.sucess(null);
		  String childFolder="big_pic";
		  String newFileName=childFolder+"_"+System.currentTimeMillis()+StringUtil.randomString(3);
		  String fileUrl=null;
		  try {
			  fileUrl= fileUtil.uploadFile(request,childFolder,newFileName);
			  JSONObject json=new JSONObject();
			  json.put("file_url",fileUrl);
			  jsonUtil.setData(json);
		} catch (SystemException e) {
			jsonUtil.setFlag(false);
			jsonUtil.setMsg("上传失败，请联系管理员");
		}
		  return jsonUtil;
	  }
	  
	  /**
	   * 上传顶部广告缩略图接口
	   * @author henrysun
	   * 2018年5月7日 上午11:42:14
	   */
	  @PostMapping(value = "uploadTinyPic")
	  @ResponseBody
	  public JsonUtil uploadTinyPic(HttpServletRequest request,HttpSession session) {
		  JsonUtil jsonUtil=JsonUtil.sucess(null);
		  String childFolder="tiny_pic";
		  String newFileName=childFolder+"_"+System.currentTimeMillis()+StringUtil.randomString(3);
		  String fileUrl=null;
		  try {
			  fileUrl= fileUtil.uploadFile(request,childFolder,newFileName);
			  JSONObject json=new JSONObject();
			  json.put("file_url",fileUrl);
			  jsonUtil.setData(json);
		} catch (SystemException e) {
			jsonUtil.setFlag(false);
			jsonUtil.setMsg("上传失败，请联系管理员");
		}
		  return jsonUtil;
	  }
	
}

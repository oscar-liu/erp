package com.whalegoods.controller;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.Device;
import com.whalegoods.entity.DeviceRoad;
import com.whalegoods.entity.GoodsSku;
import com.whalegoods.entity.request.ReqBase;
import com.whalegoods.entity.request.ReqChangePath;
import com.whalegoods.entity.request.ReqUpDeviceStatus;
import com.whalegoods.entity.request.ReqUploadLog;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.exception.BizApiException;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.ApkVersionService;
import com.whalegoods.service.DeviceRoadService;
import com.whalegoods.service.DeviceService;
import com.whalegoods.service.GoodsSkuService;
import com.whalegoods.util.CommonValidateUtil;
import com.whalegoods.util.FileUtil;

/**
 * 设备管理API
 * @author henrysun
 * 2018年5月16日 下午4:10:23
 */
@RestController
@RequestMapping(value = "/v1/device")
public class V1DeviceController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	  @Autowired
	  DeviceRoadService deviceRoadService;
	
	  @Autowired
	  DeviceService deviceService;
	  
	  @Autowired
	  ApkVersionService apkVersionService;
	  
	  @Autowired
	  GoodsSkuService goodsSkuService;
	  
	  @Autowired
	  FileUtil fileUtil;
	  
	  /**
	   * 设备状态上报接口（1服务中 2停用）
	   * @author henrysun
	   * 2018年6月3日 下午6:42:03
	   */
	  @PostMapping(value="/updateDeviceStatus")
	  ResBody updateDeviceStatus(@RequestBody ReqUpDeviceStatus model) throws SystemException {
		  logger.info("收到updateDeviceStatus请求：{}",model.toString());
		  CommonValidateUtil.validateDeviceExist(model.getDevice_code_wg(),model.getDevice_code_sup());
		  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		  Device device=new Device();
		  device.setDeviceIdJp(model.getDevice_code_wg());
		  device.setDeviceIdSupp(model.getDevice_code_sup());
		  device.setDeviceStatus(model.getDeviceStatus());
		  deviceService.updateByObjCdt(device);
		  logger.info("返回结果：{}",resBody.toString());
		  return resBody;
		}
	  
	  /**
	   * 查询设备运行状态（1正在运行 2停止运行）
	   * @author henrysun
	   * 2018年6月3日 下午2:32:20
	   */
	  @GetMapping(value="/getOperateStatus")
	  ResBody getOperateStatus(@Valid ReqBase model) {
		  logger.info("收到getOperateStatus请求：{}",model.toString());
		  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		  //查询设备运行状态
		  Device objCdt=new Device();
		  objCdt.setDeviceIdJp(model.getDevice_code_wg());
		  objCdt.setDeviceIdSupp(model.getDevice_code_sup());	 
		  Device device=deviceService.selectByObjCdt(objCdt);
		  if(device!=null){
			  //如果之前的状态是已下线，则更新为在线状态
			  if(device.getDeviceStatus()==3){
				  objCdt.setDeviceStatus((byte) 1);
				  device.setDeviceStatus((byte) 1);
			  }
			  //更新上报时间
			  objCdt.setUpTime(model.getTimestamp());
			  deviceService.updateByObjCdt(objCdt);
			  Map<String,Object> mapData=new HashMap<>();
			  mapData.put("operate_status",device.getDeviceStatus());
			  resBody.setData(mapData);
		  }
		  else
		  {
			  throw new BizApiException(ConstApiResCode.DEVICE_NOT_EXIST);
		  }
		  logger.info("返回结果：{}",resBody.toString());
		  return resBody;
		}
	  
	  /**
	   * 获取设备管理密码
	   * @author henrysun
	   * 2018年7月16日 下午4:02:30
	   */
	  @GetMapping(value = "getDevicePwd")
	  public ResBody showDeviceList(@Valid ReqBase reqBase) {
	    ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
	    Device objCdt=new Device();
	    objCdt.setDeviceIdJp(reqBase.getDevice_code_wg());
	    objCdt.setDeviceIdSupp(reqBase.getDevice_code_sup());
	    Map<String,Object> mapRst=new HashMap<>();
	    mapRst.put("pwd",deviceService.getDevicePwd(objCdt));
	    resBody.setData(mapRst);
	    return resBody;
	  }
	  
	  /**
	   * 客户端升级接口
	   * @author henrysun
	   * 2018年4月9日 上午11:05:57
	   */
	  @GetMapping(value="/updateClient")
	  ResBody updateClient(@Valid ReqBase model) {
		  return apkVersionService.getApk();
		}
	  
	  /**
	   * 上传异常文件
	   * @author 
	   * 2018年4月23日 上午10:44:47
	 * @throws SystemException 
	 * @throws FileNotFoundException 
	   */
	  @PostMapping(value="/uploadExLog")
	  public ResBody uploadExLog(@Valid ReqUploadLog model,HttpServletRequest request,HttpSession session) throws SystemException {
		  logger.info("收到uploadExLog请求：{}",model.toString());
		  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		  String childFolder="ex_log";
		  String newFileName=childFolder+"_"+model.getOrder();
		  String fileUrl=fileUtil.uploadFile(request,childFolder,newFileName);
		  logger.info("返回结果：{}",resBody.toString());
		  return resBody;
		}
	  
	  /**
	   * 更换货道商品接口
	   * @author henrysun
	   * 2018年7月17日 下午2:24:08
	   */
	  @PostMapping(value="/changePath")
	  ResBody changePath(@RequestBody ReqChangePath model) throws SystemException {
		  logger.info("收到changePath请求：{}",model.toString());
		  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		  CommonValidateUtil.validateDeviceExist(model.getDevice_code_wg(),model.getDevice_code_sup());
		  GoodsSku objCdt=new GoodsSku();
		  objCdt.setGoodsCode(model.getGoodsCode());
		  List<GoodsSku> lstGoodsSku=goodsSkuService.selectListByObjCdt(objCdt);
		  if(lstGoodsSku.size()==0){
			  throw new BizApiException(ConstApiResCode.GOODS_CODE_NOT_EXIST);
		  }
		  if(lstGoodsSku.size()>1){
			  throw new BizApiException(ConstApiResCode.GOODS_CODE_NOT_ONLY_ONE);
		  }
		  DeviceRoad objCdt2=new DeviceRoad();
		  BeanUtils.copyProperties(model,objCdt2);
		  objCdt2.setGoodsSkuId(lstGoodsSku.get(0).getId());
		  objCdt2.setDeviceIdJp(model.getDevice_code_wg());
		  objCdt2.setDeviceIdSupp(model.getDevice_code_sup());
		  Map<String,Object> mapCdt=new HashMap<>();
		  mapCdt.put("goodsCode",model.getGoodsCode());
		  objCdt2.setSalePrice(deviceRoadService.selectMaxPriceByGoodsCode(mapCdt));
		  deviceRoadService.updateByObjCdt(objCdt2);
		  logger.info("返回结果：{}",resBody.toString());
		  return resBody;
		}
}

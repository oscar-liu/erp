package com.whalegoods.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.whalegoods.common.ResBody;
import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.Device;
import com.whalegoods.entity.request.ReqBase;
import com.whalegoods.entity.request.ReqUpDeviceStatus;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.DeviceService;

/**
 * 设备管理API
 * @author henry-sun
 *
 */
@RestController
@RequestMapping(value = "/v1/device")
public class V1DeviceController  extends BaseController<Object>{

  @Autowired
  DeviceService deviceService;
  
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
	  deviceService.updateDevice(device);	  
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
	  Map<String,Object> condition=new HashMap<>();
	  condition.put("deviceIdJp",model.getDevice_code_wg());
	  condition.put("deviceIdSupp",model.getDevice_code_sup());
	  int status=deviceService.getOperateStatus(condition);
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
  public ResBody uploadFile(@RequestParam(name="order") String orderId,HttpServletRequest request,HttpSession session) throws SystemException, FileNotFoundException {
	  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
	  
	  //不是multipart/form-data类型
	  if(!ServletFileUpload.isMultipartContent(request)){  
          throw new SystemException(ConstApiResCode.SYSTEM_ERROR,"表单的enctype属性不是multipart/form-data类型！！");
      }
	  
      //创建上传所需要的两个对象
      DiskFileItemFactory factory = new DiskFileItemFactory();
      //解析器依赖于工厂
      ServletFileUpload sfu = new ServletFileUpload(factory);
      //创建容器来接受解析的内容
      List<FileItem> items = new ArrayList<FileItem>();
      //将上传的文件信息放入容器中
      try {
          items = sfu.parseRequest(request);
      } catch (FileUploadException e) {
          e.printStackTrace();
      }
      
      for(FileItem item : items){
    	  try {
			handleUploadField(item);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
	    return resBody;
	}
  
  private void handleUploadField(FileItem item) throws FileNotFoundException {
      String fileName = item.getName();  //得到上传文件的文件名
      if(fileName!=null && !"".equals(fileName)){
          //控制只能上传图片
          if(!item.getContentType().startsWith("image")){
              return;
          }

          //向控制台打印文件信息
          System.out.println("fileName:"+fileName);
          System.out.println("fileSize:"+item.getSize());
      }

      //上传文件存储路径
      String path = ResourceUtils.getFile("D:/whalegoods-file/ex_log/").getPath();
      //创建子目录
      File childDirectory = getChildDirectory(path);

      //写入服务器或者磁盘
      try {
          item.write(new File(childDirectory.toString(),UUID.randomUUID()+"_"+fileName));
      } catch (Exception e) {
          e.printStackTrace();
      }

  }
  
  private File getChildDirectory(String path) {
      Date currTime = new Date();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      String time = sdf.format(currTime);

      File file = new File(path,time);
      if(!file.exists()){
          file.mkdirs();
      }
      return file;
  }
  
}

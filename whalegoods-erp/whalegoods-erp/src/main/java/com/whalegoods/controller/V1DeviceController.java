package com.whalegoods.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
   */
  @PostMapping(value="/uploadExLog")
  public ResBody uploadFile(@RequestParam(name="order") String orderId,HttpServletRequest request,HttpSession session) throws SystemException {
	  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
	    // 临时文件路径
	   String dirTemp = "/ex_log";
	    //图片存储相对路径
	   String suitelogo =dirTemp;
	   String realPath=null;
	   
		try {
			realPath = ResourceUtils.getFile("classpath:static").getPath();
		} catch (FileNotFoundException e1) {
			throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
		}
	    
	    File realFile = new File(realPath);
	    
	    String rootPath = realFile.getPath();
	    String normPath = rootPath + suitelogo;
	    String tempPath = rootPath + dirTemp;

	    File f = new File(normPath);//存储路径
	    File f1 = new File(tempPath);//临时路径
	    if (!f.exists()) {
	        f.mkdirs();
	    }

	    if (!f1.exists()) {
	        f1.mkdirs();
	    }

	    
	    //创建文件解析对象
	    DiskFileItemFactory factory = new DiskFileItemFactory();
	    
	    //设定使用内存超过5M时，将产生临时文件并存储于临时目录中 
	    factory.setSizeThreshold(5 * 1024 * 1024); 
	    
	    // 设定存储临时文件的目录
	    factory.setRepository(f1); 

	    //创建上传文件实例
	    ServletFileUpload upload = new ServletFileUpload(factory);
	    upload.setHeaderEncoding("UTF-8");
	    try {
            File uploadedFile = new File(normPath + "/" + orderId+".png");
            OutputStream os = new FileOutputStream(uploadedFile);
            InputStream is = request.getInputStream();
            byte buf[] = new byte[1024];// 可以修改 1024 以提高读取速度
            int length = 0;
            while ((length = is.read(buf)) > 0) {
                os.write(buf, 0, length);
            }
            // 关闭流
            os.flush();
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	    return resBody;
	}
  
}

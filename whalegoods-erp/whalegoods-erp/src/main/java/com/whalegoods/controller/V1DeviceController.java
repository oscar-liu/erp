package com.whalegoods.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
   */
  @PostMapping(value="/uploadExLog")
  public ResBody uploadFile(String orderid,HttpServletRequest request, HttpServletResponse response, HttpSession session)
	        throws Exception {
	  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
	    String type = request.getParameter("type");
	    String ret_fileName = null;// 返回给前端已修改的图片名称

	    // 临时文件路径
	    String dirTemp = "/static/upload/temp";
	    
	    //图片存储相对路径
	    String suitelogo = "";
	    
	    //设置图片存储相对路径
	    //这里的type是前台传过来方便辨认是什么图片的参数，没有需要就不用判断
	    if ("app".equals(type)) {
	        suitelogo = "/static/upload/applogo";
	    } else if ("suite".equals(type)) {
	        suitelogo = "/static/upload/suitelogo";
	    } else {
	        suitelogo = dirTemp;
	    }

	    // 获取当前项目的根目录  tomcat的绝对路径/webapps/项目名
	    String realPath = session.getServletContext().getRealPath("");
	    
	    
	    //获取tomcat下的ROOT目录，通过root绝对路径和存储图片文件夹的相对路径创建目录 
	    //mkdirs方法逐级创建目录。 mkdir只创建最后一级目录，前面目录不存在则不创建
	    File realFile = new File(realPath);
	    
	    String rootPath = realFile.getParent() + "/ROOT";
	    String normPath = rootPath + suitelogo;
	    String tempPath = rootPath + dirTemp;

	    File f = new File(normPath);
	    File f1 = new File(tempPath);
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
	    factory.setRepository(new File(tempPath)); 

	    //创建上传文件实例
	    ServletFileUpload upload = new ServletFileUpload(factory);
	    upload.setHeaderEncoding("UTF-8");
	    try {
            File uploadedFile = new File(normPath + "/" + getUUID());
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
  
  private String getUUID() {
	    return UUID.randomUUID().toString();
	}
  
}

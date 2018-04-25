package com.whalegoods.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.exception.SystemException;

/**
 * 文件处理工具类
 * @author chencong
 * 2018年4月23日 上午11:28:56
 */
@Component
public class FileUtil {

	@Autowired
	private  Environment env;

	public String getLocation() {
		return env.getProperty("spring.http.multipart.location");
	}
	
	/**
	 * 上传文件
	 * @author chencong
	 * 2018年4月25日 上午10:34:34
	 * @throws SystemException 
	 */
	public  void uploadFile(HttpServletRequest request,String sonFolder,String newFileName) throws SystemException{
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
	    	throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
	    }
	    for(FileItem item : items){
	    	try {
				handleUploadField(item,sonFolder,newFileName);
			} catch (FileNotFoundException e) {
				throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
			}
	    }
	}
    
    private  void handleUploadField(FileItem item,String sonFolder,String newFileName) throws FileNotFoundException, SystemException {
    	//得到上传文件的文件名
        String fileName = item.getName();  
        //上传文件存储路径
        String path = ResourceUtils.getFile(getLocation()).getPath();
        //创建子目录
        File childDirectory =getChildDirectory(path,sonFolder);
        //写入服务器或者磁盘
        try {
			item.write(new File(childDirectory.toString(),newFileName+fileName.substring(fileName.lastIndexOf("."))));
		} catch (Exception e) {
			throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
		}
    }
    
    /**
     * 创建子目录
     * @author chencong
     * 2018年4月25日 上午10:43:24
     */
    private  File getChildDirectory(String path,String sonFolder ) {
        File file = new File(path,sonFolder);
        if(!file.exists()){
            file.mkdirs();
        }
        return file;
    }
}

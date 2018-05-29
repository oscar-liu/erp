package com.whalegoods.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.exception.SystemException;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;

/**
 * 文件处理工具类
 * @author henrysun
 * 2018年4月25日 下午7:56:25
 */
@Component
public class FileUtil {

	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
	
	@Autowired
	private  Environment env;

	public String getLocation() {
		return env.getProperty("spring.http.multipart.location");
	}
	
	public String getAddr() {
		return env.getProperty("spring.http.multipart.addr");
	}
	
	/**
	 * 上传文件，并得到文件的访问路径
	 * @author henrysun
	 * 2018年5月29日 下午3:10:42
	 */
	public  String uploadFile(HttpServletRequest request,String sonFolder,String newFileName) throws SystemException{
		//如果不是multipart/form-data类型
		 if(!ServletFileUpload.isMultipartContent(request)){  
			logger.error("请求类型不是multipart/form-data");
	        throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
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
	    	logger.error("ServletFileUpload解析request异常："+e.getMessage());
	    	throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
	    }
	    //新的带后缀名的文件名
	    String newWholeFileName=null;
	    for(FileItem item : items){
	    	try {
	    		newWholeFileName=this.handleUploadField(item,sonFolder,newFileName);
			} catch (FileNotFoundException e) {
				logger.error("执行handleUploadField()方法异常："+e.getMessage());
				throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
			}
	    }
	    return getAddr()+sonFolder+"/"+newWholeFileName;
	}
    
    private  String handleUploadField(FileItem item,String sonFolder,String newFileName) throws FileNotFoundException, SystemException {
    	//得到上传文件的文件名
        String fileName = item.getName();  
        //上传文件存储路径
        String path = ResourceUtils.getFile(getLocation()).getPath();
        //创建子目录
        File childDirectory =this.getChildDirectory(path,sonFolder);
        //新的带后缀名的文件名
        String newWholeFileName=null;
        //写入服务器或者磁盘
        try {
        	newWholeFileName=newFileName+fileName.substring(fileName.lastIndexOf("."));
			item.write(new File(childDirectory.toString(),newWholeFileName));
		} catch (Exception e) {
			throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
		}
        return newWholeFileName;
    }
    
    /**
     * 创建子目录
     * @author henrysun
     * 2018年5月29日 下午3:10:33
     */
    private  File getChildDirectory(String path,String sonFolder ) {
        File file = new File(path,sonFolder);
        if(!file.exists()){
            file.mkdirs();
        }
        return file;
    }
    
    
    /**
     * 导出EXCEL
     * @author henrysun
     * 2018年5月17日 上午11:26:44
     */
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass,String fileName, HttpServletResponse response) throws SystemException{
        defaultExport(list, pojoClass, fileName, response, new ExportParams(title, sheetName));
    }
    
    private static void defaultExport(List<?> list, Class<?> pojoClass, String fileName, HttpServletResponse response, ExportParams exportParams) throws SystemException {
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams,pojoClass,list);
        if (workbook != null);
        downLoadExcel(fileName, response, workbook);
    }

    private static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) throws SystemException {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition","attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
        }
    }
}

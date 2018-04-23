package com.whalegoods.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.exception.SystemException;

/**
 * 文件处理工具类
 * @author chencong
 * 2018年4月23日 上午11:28:56
 */
public class FileUtil {

	public static void uploadFile(byte[] file, String filePath, String fileName) throws SystemException { 
        File targetFile = new File(filePath);  
        if(!targetFile.exists()){    
            targetFile.mkdirs();    
        }       
       try {
    	   FileOutputStream out = new FileOutputStream(filePath+fileName);
           out.write(file);
           out.flush();
           out.close();
	} catch (IOException e) {
		throw new SystemException(ConstApiResCode.SYSTEM_ERROR,e.getMessage());
	}
       
    }
}

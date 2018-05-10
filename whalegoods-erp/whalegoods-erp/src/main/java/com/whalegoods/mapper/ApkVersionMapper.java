package com.whalegoods.mapper;

import java.util.Map;

import com.whalegoods.entity.ApkVersion;

public interface ApkVersionMapper extends BaseMapper<ApkVersion, String>{

	/**获取最新的APK包
	 * @author henrysun
	 * 2018年4月28日 下午2:46:35
	 */
    Map<String,Object> getApk();
    
    /**
     * 校验APK版本是否存在
     * @author henrysun
     * 2018年5月8日 下午3:54:57
     */
    int checkApkVersion(String apkVersion);
	
}
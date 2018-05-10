package com.whalegoods.service;

import com.whalegoods.entity.ApkVersion;
import com.whalegoods.entity.response.ResBody;

/**
 * 柜机APK相关业务逻辑接口
 * @author henrysun
 * 2018年4月28日 下午4:41:45
 */
public interface ApkVersionService extends BaseService<ApkVersion,String> {

	  /**
	   * 查询最新柜机APK包版本号和下载链接
	   * @author chencong
	   * 2018年4月9日 下午3:16:05
	   */
	  ResBody getApk();
	  
	  int checkApkVersion(String apkVersion);
}

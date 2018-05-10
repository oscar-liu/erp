package com.whalegoods.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.ApkVersion;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.mapper.ApkVersionMapper;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.service.ApkVersionService;


@Service
public class ApkVersionServiceImpl extends BaseServiceImpl<ApkVersion,String> implements ApkVersionService {
	
	  @Autowired
	  ApkVersionMapper apkVersionMapper;

	  @Override
	  public BaseMapper<ApkVersion,String> getMapper() {
	    return apkVersionMapper;
	  }
	  
	  @Override
		public ResBody getApk() {
			ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
			resBody.setData(apkVersionMapper.getApk());
			return resBody;
		}

	@Override
	public int checkApkVersion(String apkVersion) {
		return apkVersionMapper.checkApkVersion(apkVersion);
	}

}

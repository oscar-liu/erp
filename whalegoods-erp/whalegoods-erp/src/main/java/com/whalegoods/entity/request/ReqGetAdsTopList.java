package com.whalegoods.entity.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 获取广告列表请求映射实体类
 * @author henrysun
 * 2018年5月15日 下午4:24:00
 */
@Getter
@Setter
@ToString
public class ReqGetAdsTopList extends ReqBase implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String deviceId;

}

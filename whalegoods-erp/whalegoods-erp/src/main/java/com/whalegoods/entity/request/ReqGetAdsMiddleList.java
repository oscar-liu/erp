package com.whalegoods.entity.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 获取促销列表请求映射实体类
 * @author henrysun
 * 2018年5月15日 上午11:36:54
 */
@Getter
@Setter
@ToString
public class ReqGetAdsMiddleList extends ReqBase implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String deviceId;

}

package com.whalegoods.entity.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 柜机APP获取促销商品信息API other部分返回实体类
 * @author henrysun
 * 2018年5月15日 上午10:42:06
 */
@Getter
@Setter
@ToString
public class ResGoodsAdsMiddleOther implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("start_time")
	private String startTime;
	
	@JsonProperty("end_time")
	private String endTime;
	
	//1灯亮2灯灭
	private Byte type;
}
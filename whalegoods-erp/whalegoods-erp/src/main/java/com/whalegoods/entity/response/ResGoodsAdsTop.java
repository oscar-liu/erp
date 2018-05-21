package com.whalegoods.entity.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 顶部广告栏商品信息获取接口API  返回结果data字段集合  实体类
 * @author henrysun
 * 2018年5月3日 上午11:24:15
 */
@Getter
@Setter
@ToString
public class ResGoodsAdsTop implements Serializable {
	
	private static final long serialVersionUID = 1L;
    
	@JsonProperty("action_content")
    private String goodsCode;
    
	@JsonProperty("big_pic_url")
    private String bigPicUrl;
    
	@JsonProperty("tiny_pic_url")
    private String tinyPicUrl;
	
	@JsonProperty("action_type")
	private Byte actionType;

}
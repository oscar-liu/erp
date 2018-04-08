package com.whalegoods.entity.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class ResGoodsAdsTop implements Serializable {
	
	private static final long serialVersionUID = 1L;
    
	@JsonProperty("goods_code")
    private String GoodsCode;
    
	@JsonProperty("big_pic_url")
    private String bigPicUrl;
    
	@JsonProperty("tiny_pic_url")
    private String tinyPicUrl;

}
package com.whalegoods.entity.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 根据设备编号获取所有商品信息列表接口API  返回结果data字段  实体类
 * @author chencong
 * 2018年4月9日 下午3:04:12
 */
@Getter
@Setter
@ToString
public class ResDeviceGoodsInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("goods_code")
	private String goodsCode;
	
	@JsonProperty("goods_name")
	private String goodsName;
	
	private Byte ctn;
	
	private String spec;
	
	@JsonProperty("goods_detail")
	private String goodsDetail;
	
	@JsonProperty("made_in")
	private String madeIn;
	
	@JsonProperty("market_price")
	private Double marketPrice;
	
	@JsonProperty("sale_price")
	private Double salePrice;
	
	@JsonProperty("path_code")
	private String pathCode;
	
	private Byte floor;
	
	@JsonProperty("pic_url")
	private String picUrl;
	
	private Integer stock;
}

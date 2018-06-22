package com.whalegoods.entity.response;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 根据设备编号获取所有商品信息列表接口API  返回结果data字段  实体类
 * @author henrysun
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
	
	@JsonProperty("goods_name_en")
	private String goodsNameEn;
	
	private Byte ctn;
	
	private String spec;
	
	@JsonProperty("goods_detail")
	private String goodsDetail;
	
	@JsonProperty("goods_detail_en")
	private String goodsDetailEn;
	
	@JsonProperty("made_in")
	private String madeIn;
	
	@JsonProperty("market_price")
	private Double marketPrice;
	
	@JsonProperty("sale_price")
	private Double salePrice;
	
	@JsonProperty("path_code")
	private Byte pathCode;
	
	private Byte floor;
	
	@JsonProperty("pic_url")
	private String picUrl;
	
	private Integer stock;
	
	@JsonProperty("sale_type")
	private Byte saleType;
	
	@JsonProperty("ads_middle_type")
	private Byte adsMiddleType;
	
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    
    private String startHms;
    
    private String endHms;

    //如果是促销商品，该字段取代salePrice字段作为当前销售价
	private Double mSalePrice;
}

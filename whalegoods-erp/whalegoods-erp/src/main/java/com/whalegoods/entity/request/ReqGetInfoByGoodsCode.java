package com.whalegoods.entity.request;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 根据当前设备商品编号获取商品信息接口API  请求映射实体类 
 * @author chencong
 * 2018年4月9日 下午5:16:17
 */
@Getter
@Setter
@ToString
public class ReqGetInfoByGoodsCode extends ReqBase implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("goods_code")
	@NotEmpty(message="商品编号不能为空")
	private String goods_code;

}

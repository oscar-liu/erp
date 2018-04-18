package com.whalegoods.entity.request;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 根据当前设备商品编号或货道号获取商品信息接口API  请求映射实体类 
 * @author chencong
 * 2018年4月9日 下午5:16:17
 */
@Getter
@Setter
@ToString
public class ReqGetInfoByPathOrGoodsCode extends ReqBase implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("goods_code")
	private String goods_code;
	
	@JsonProperty("path_code")
	private Byte path_code;
	
	@NotNull(message="编号类型不能为空")
	@Min(1)
	@Max(2)
	private Byte type;

}

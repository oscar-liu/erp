package com.whalegoods.entity.request;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * 更换货道商品API请求映射实体类
 * @author henrysun
 * 2018年7月17日 下午2:17:20
 */
@Getter
@Setter
public class ReqChangePath extends ReqBase{

	private static final long serialVersionUID = 1L;
	
	@NotNull(message="ctn必填")
	private Byte ctn;
	
	@NotNull(message="floor必填")
	private Byte floor;
	
	
	@JsonProperty("path_code")
	@NotNull(message="path_code必填")
	private Byte pathCode;
	
	@JsonProperty("goods_code")
	@NotEmpty(message="goods_code必填")
	private String goodsCode;

	@Override
	public String toString() {
		return "ReqChangePath [ctn=" + ctn + ", floor=" + floor + ", pathCode=" + pathCode + ", goodsCode=" + goodsCode
				+", device_code_sup=" + super.getDevice_code_sup() +", device_code_wg=" + super.getDevice_code_wg() + "]";
	}
	
}

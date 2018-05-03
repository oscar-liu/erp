package com.whalegoods.entity.request;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 上传日志文件API映射实体类
 * @author henrysun
 * 2018年5月3日 下午3:12:55
 */
@Getter
@Setter
@ToString
public class ReqUploadLog extends ReqBase implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message="order必填")
	@Length(min=32,max=32,message="订单号长度不正确")
	private String order;
	
	@JsonProperty("error_message")
	@NotEmpty(message="error_message必填")
	private String error_message;
}

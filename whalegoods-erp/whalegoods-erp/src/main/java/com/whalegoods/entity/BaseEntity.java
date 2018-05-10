package com.whalegoods.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 公共字段实体类
 * @author henry-sun
 *
 */
@Getter
@Setter
@ToString
public class BaseEntity {
	
	private  String id;

    private  String createBy;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private  Date createDate;
    
    private  String updateBy;
    
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private  Date updateDate;
    
    private  String remark;
}

package com.whalegoods.entity;

import java.util.Date;

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

    private  Date createDate;
    
    private  String updateBy;
    
    private  Date updateDate;
    
    private  String remark;
}

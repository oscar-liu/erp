package com.whalegoods.entity.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 任务通知列表和开关状态
 * @author henrysun
 * 2018年6月7日 下午4:16:53
 */
@Getter
@Setter
@ToString
public class ResJobEmail implements Serializable {

	private static final long serialVersionUID = 1L;
    
    private String email;
    
    private Byte switch_status;
}

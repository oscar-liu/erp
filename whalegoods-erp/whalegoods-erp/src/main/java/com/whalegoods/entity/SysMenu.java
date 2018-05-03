package com.whalegoods.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统菜单表实体类
 * @author henrysun
 * 2018年5月3日 下午5:59:36
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SysMenu implements Serializable {
	
    private String id;

    private String menuName;

    private String pId;

    private String menuUrl;

    private Integer orderNum;

    private String icon;

    private String createBy;

    private Date createDate;

    private String updateBy;

    private Date updateDate;

    private String permission;

    private Byte menuType;
    
    /**菜单排序id 填充菜单展示id*/
    private int num;

    private List<SysRole> roleList;

    private static final long serialVersionUID = 1L;

    private List<SysMenu> children=new ArrayList<SysMenu>();

    public void addChild(SysMenu sysMenu){
        children.add(sysMenu);
    }
}
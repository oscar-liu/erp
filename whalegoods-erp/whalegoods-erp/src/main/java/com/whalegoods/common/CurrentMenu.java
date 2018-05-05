package com.whalegoods.common;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 当前菜单实体类
 * @author henrysun
 * 2018年5月2日 上午11:21:51
 */
@Getter
@Setter
@ToString
public class CurrentMenu {
	
    private String id;

    private String name;

    private String pId;

    private String url;

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

    public CurrentMenu(String id, String name, String pId, String url, Integer orderNum, String icon, String permission, Byte menuType,Integer num) {
        this.id = id;
        this.name = name;
        this.pId = pId;
        this.url = url;
        this.orderNum = orderNum;
        this.icon = icon;
        this.permission = permission;
        this.menuType = menuType;
        this.num=num;
    }
}

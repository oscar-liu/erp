package com.whalegoods.common;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.whalegoods.common.CurrentMenu;
import com.whalegoods.common.CurrentRole;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 当前系统用户实体类
 * @author henrysun
 * 2018年5月3日 下午5:25:45
 */
@Getter
@Setter
@ToString
public class CurrentUser implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    private String id;

    private String username;

    private String password;

    private String headPicUrl;

    private Byte accountStatus;

    private String createBy;

    private String updateBy;

    private Date createDate;

    private Date updateDate;

   private List<CurrentMenu> currentMenuList;
   
   private List<CurrentRole> currentRoleList;

   public CurrentUser(String id, String username, String headPicUrl,Byte accountStatus) {
        this.id = id;
        this.username = username;
        this.headPicUrl = headPicUrl;
        this.accountStatus = accountStatus;
    }

    public CurrentUser() {}
}
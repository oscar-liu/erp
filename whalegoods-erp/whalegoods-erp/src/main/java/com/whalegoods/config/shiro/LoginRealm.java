package com.whalegoods.config.shiro;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.whalegoods.common.CurrentMenu;
import com.whalegoods.common.CurrentRole;
import com.whalegoods.common.CurrentUser;
import com.whalegoods.entity.SysMenu;
import com.whalegoods.entity.SysRole;
import com.whalegoods.entity.SysUser;
import com.whalegoods.service.MenuService;
import com.whalegoods.service.SysUserService;
import com.whalegoods.util.ShiroUtil;


@Service
public class LoginRealm extends AuthorizingRealm{

  @Autowired
  private SysUserService userService;

  @Autowired
  private MenuService menuService;

  /**
   * 获取认证
   * @param principalCollection
   * @return
   */
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
    //根据用户获取角色 根据角色获取所有按钮权限
   CurrentUser cUser= (CurrentUser) ShiroUtil.getSession().getAttribute("currentUser");
   for(CurrentRole cRole:cUser.getCurrentRoleList()){
     info.addRole(cRole.getId());
   }
   for(CurrentMenu cMenu:cUser.getCurrentMenuList()){
     if(!StringUtils.isEmpty(cMenu.getPermission()))
     info.addStringPermission(cMenu.getPermission());
   }
    return info;
  }

  /**
   * 获取授权
   * @author henrysun
   * 2018年5月3日 下午5:25:14
   */
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
	    String username=(String)authenticationToken.getPrincipal();
	    SysUser s=userService.login(username);
	    if(s==null){
	      throw new UnknownAccountException("账户密码不正确");
	    }else{
	      CurrentUser currentUser=new CurrentUser(s.getId(),s.getUserName(),s.getHeadPicUrl(),s.getAccountStatus());
	      Subject subject = ShiroUtil.getSubject();
	      /**角色权限封装进去*/
	      //根据用户获取菜单
	      List<SysMenu> menuList=new ArrayList<>(new HashSet<>(menuService.getUserMenu(s.getId())));
	      JSONArray json=menuService.getMenuJsonByUser(menuList);
	      Session session= subject.getSession();
	      session.setAttribute("menu",json);
	      CurrentMenu currentMenu=null;
	      List<CurrentMenu> currentMenuList=new ArrayList<>();
	      List<SysRole> roleList=new ArrayList<>();
	      for(SysMenu m:menuList){
	       currentMenu=new CurrentMenu(m.getId(),m.getName(),m.getPId(),m.getMenuUrl(),m.getOrderNum(),m.getIcon(),m.getPermission(),m.getMenuType(),m.getNum());
	        currentMenuList.add(currentMenu);
	          roleList.addAll(m.getRoleList());
	      }
	      roleList= new ArrayList<>(new HashSet<>(roleList));
	      List<CurrentRole> currentRoleList=new ArrayList<>();
	      CurrentRole role=null;
	      for(SysRole r:roleList){
	        role=new CurrentRole(r.getId(),r.getRoleName(),r.getRemark());
	        currentRoleList.add(role);
	      }
	      currentUser.setCurrentRoleList(currentRoleList);
	      currentUser.setCurrentMenuList(currentMenuList);
	      session.setAttribute("currentUser",currentUser);
	    }
	    ByteSource byteSource=ByteSource.Util.bytes(username);
	    return new SimpleAuthenticationInfo(username,s.getPassword(), byteSource, getName());
  }
}

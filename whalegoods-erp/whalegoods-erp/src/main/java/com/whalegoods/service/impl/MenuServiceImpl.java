package com.whalegoods.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.whalegoods.entity.SysMenu;
import com.whalegoods.entity.SysRoleMenu;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.SysMenuMapper;
import com.whalegoods.mapper.SysRoleMenuMapper;
import com.whalegoods.service.MenuService;
import com.whalegoods.util.TreeUtil;

/**
 * 菜单业务逻辑实现层
 * @author henrysun
 * 2018年5月2日 上午11:41:38
 */
@Service
public class MenuServiceImpl extends BaseServiceImpl<SysMenu,String> implements MenuService {
	
	 @Autowired
	  private SysMenuMapper sysMenuMapper;

	  @Autowired
	  private SysRoleMenuMapper roleMenuMapper;

	  @Override
	  public BaseMapper<SysMenu, String> getMapper() {
	    return sysMenuMapper;
	  }

	  @Override
	  public List<SysMenu> getMenuNotSuper() {
	    return sysMenuMapper.getMenuNotSuper();
	  }

	  @Override
	  public List<SysMenu> getMenuChildren(String id) {
	    return sysMenuMapper.getMenuChildren(id);
	  }

	  @Override
	  public JSONArray getMenuJson(String roleId){
	    List<SysMenu> mList=sysMenuMapper.getMenuNotSuper();
	    JSONArray jsonArr=new JSONArray();
	    int pNum=1000,num=0;
	    for(SysMenu sysMenu:mList){
	      SysMenu menu=getChild(sysMenu.getId(),true,pNum,num);
	      jsonArr.add(menu);
	      pNum+=1000;
	    }
	    System.out.println(jsonArr);
	    return jsonArr;
	  }

	  @Override
	  public JSONArray getMenuJsonList() {
	    List<SysMenu> mList=sysMenuMapper.getMenuNotSuper();
	    JSONArray jsonArr=new JSONArray();
	    for(SysMenu sysMenu:mList){
	      SysMenu menu=getChild(sysMenu.getId(),false,0,0);
	      jsonArr.add(menu);
	    }
	    return jsonArr;
	  }

	  @Override
	  public JSONArray getMenuJsonByUser(List<SysMenu> menuList){
	    JSONArray jsonArr=new JSONArray();
	    Collections.sort(menuList, new Comparator<SysMenu>() {
	      @Override
	      public int compare(SysMenu o1, SysMenu o2) {
	        return o1.getOrderNum().compareTo(o2.getOrderNum());
	      }
	    });
	    int pNum=1000;
	    for(SysMenu menu:menuList){
	      if(StringUtils.isEmpty(menu.getPId())){
	        SysMenu sysMenu=getChilds(menu,pNum,0,menuList);
	        jsonArr.add(sysMenu);
	        pNum+=1000;
	      }
	    }
	    return jsonArr;
	  }

	  @Override
	  public JSONArray getTreeUtil(String roleId){
	    TreeUtil treeUtil=null;
	    List<SysMenu> mList=sysMenuMapper.getMenuNotSuper();
	    JSONArray jsonArr=new JSONArray();
	    for(SysMenu sysMenu:mList){
	      treeUtil=getChildByTree(sysMenu.getId(),false,0,null,roleId);
	      jsonArr.add(treeUtil);
	    }
	    System.out.println(jsonArr);
	    return jsonArr;

	  }

	  @Override
	  public List<SysMenu> getUserMenu(String userId) {
	    return sysMenuMapper.getUserMenu(userId);
	  }
	  
	  @Override
	  public List<SysMenu> getMenuChildrenAll(String id) {
	    return sysMenuMapper.getMenuChildrenAll(id);
	  }

	  private SysMenu getChilds(SysMenu menu,int pNum,int num,List<SysMenu> menuList){
		    for(SysMenu menus:menuList){
		      if(menu.getId().equals(menus.getPId())&&menus.getMenuType()==0){
		        ++num;
		        SysMenu m=getChilds(menus,pNum,num,menuList);
		        m.setNum(pNum+num);
		        menu.addChild(m);
		      }
		    }
		    return menu;

		  }

		  /**
		   *
		   * @param id 父菜单id
		   * @param flag true 获取非按钮菜单 false 获取包括按钮在内菜单 用于nemuList展示
		   * @param pNum 用户控制侧拉不重复id tab 父循环+1000
		   * @param num 用户控制侧拉不重复id tab 最终效果 1001 10002 2001 2002
		   * @return
		   */
		  private SysMenu getChild(String id,boolean flag,int pNum,int num){
		    SysMenu sysMenu=sysMenuMapper.selectById(id);
		    List<SysMenu> mList=null;
		    if(flag){
		      mList= sysMenuMapper.getMenuChildren(id);
		    }else{
		      mList=sysMenuMapper.getMenuChildrenAll(id);
		    }
		    for(SysMenu menu:mList){
		      ++num;
		      SysMenu m=getChild(menu.getId(),flag,pNum,num);
		      if(flag)
		        m.setNum(pNum+num);
		      sysMenu.addChild(m);
		    }
		    return sysMenu;
		  }
		  
		  private TreeUtil getChildByTree(String id,boolean flag,int layer,String pId,String roleId){
			    layer++;
			    SysMenu sysMenu=sysMenuMapper.selectById(id);
			    List<SysMenu> mList=null;
			    if(flag){
			      mList= sysMenuMapper.getMenuChildren(id);
			    }else{
			      mList=sysMenuMapper.getMenuChildrenAll(id);
			    }
			    TreeUtil treeUtil = new TreeUtil();
			    treeUtil.setId(sysMenu.getId());
			    treeUtil.setName(sysMenu.getName());
			    treeUtil.setLayer(layer);
			    treeUtil.setPId(pId);
			    /**判断是否存在*/
			    if(!StringUtils.isEmpty(roleId)){
			      SysRoleMenu sysRoleMenu = new SysRoleMenu();
			      sysRoleMenu.setMenuId(sysMenu.getId());
			      sysRoleMenu.setRoleId(roleId);
			      int count = roleMenuMapper.selectCountByObjCdt(sysRoleMenu);
			      if (count > 0)
			        treeUtil.setChecked(true);
			    }
			    for(SysMenu menu:mList){
			      TreeUtil m=getChildByTree(menu.getId(),flag,layer,menu.getId(),roleId);
			      treeUtil.getChildren().add(m);
			    }
			    return treeUtil;
			  }
}

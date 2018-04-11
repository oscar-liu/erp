package com.whalegoods.controller;

import com.alibaba.fastjson.JSONArray;
import com.whalegoods.config.log.Log;
import com.whalegoods.entity.SysMenu;
import com.whalegoods.exception.BizApiException;
import com.whalegoods.service.MenuService;
import com.whalegoods.util.JsonUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 菜单API
 * @author chencong
 * 2018年4月11日 上午9:34:02
 */
@Controller
@RequestMapping(value = "/menu")
public class MenuController extends BaseController<Object>{

  @Autowired
  private MenuService menuService;

  /**
   * 展示tree
   * @param model
   * @return
   */
  @Log(desc = "展示菜单",type = Log.LOG_TYPE.SELECT)
  @GetMapping(value = "showMenu")
  @RequiresPermissions("menu:show")
  public String showMenu(Model model){
    JSONArray ja=menuService.getMenuJsonList();
    model.addAttribute("menus", ja.toJSONString());
    return "/system/menu/menuList";
  }

  @GetMapping(value = "showAddMenu")
  public String addMenu(Model model){
    JSONArray ja=menuService.getMenuJsonList();
    System.out.print(ja.toJSONString());
    model.addAttribute("menus", ja.toJSONString());
    return "/system/menu/add-menu";
  }

  @PostMapping(value = "addMenu")
  @ResponseBody
  public JsonUtil addMenu(SysMenu sysMenu,Model model){
	  if(StringUtils.isEmpty(sysMenu.getPId())){
      sysMenu.setPId(null);
    }
    if(StringUtils.isEmpty(sysMenu.getUrl())){
      sysMenu.setUrl(null);
    }
    if(StringUtils.isEmpty(sysMenu.getPermission())){
      sysMenu.setPermission(null);
    }
    JsonUtil jsonUtil=new JsonUtil();
    if(sysMenu==null){
      jsonUtil.setMsg("获取数据失败");
      return jsonUtil;
    }
    try{
      if(sysMenu.getMenuType()==2){
        sysMenu.setMenuType((byte)0);
      }
      if(menuService.insertSelective(sysMenu)!=0){
          jsonUtil.setFlag(true);
          jsonUtil.setMsg("添加成功");
      }
      else {
    	  jsonUtil.setFlag(false);
          jsonUtil.setMsg("添加失败");
	}
    }catch (BizApiException e){
      e.printStackTrace();
      jsonUtil.setMsg("添加失败");
    }
    return jsonUtil;
  }

}

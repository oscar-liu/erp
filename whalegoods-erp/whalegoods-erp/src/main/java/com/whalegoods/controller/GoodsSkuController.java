package com.whalegoods.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.GoodsAdsMiddle;
import com.whalegoods.entity.GoodsSku;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.GoodsAdsMiddleService;
import com.whalegoods.service.GoodsSkuService;
import com.whalegoods.util.FileUtil;
import com.whalegoods.util.JsonUtil;
import com.whalegoods.util.ReType;
import com.whalegoods.util.ShiroUtil;
import com.whalegoods.util.StringUtil;


/**
 * 后台促销管理相关接口
 * @author henrysun
 * 2018年5月14日 下午3:18:28
 */
@Controller
@RequestMapping(value = "/ads")
public class GoodsSkuController  {
	
	  @Autowired
	  GoodsAdsMiddleService adsMiddleService; 

	  /**
	   * 跳转到促销列表
	   * @author henrysun
	   * 2018年5月7日 上午10:12:39
	   */
	  @GetMapping(value = "showAdsMiddle")
	  @RequiresPermissions("ads:middle:list")
	  public String showAdsMiddle(Model model) {
	    return "/ads/middle/middleList";
	  }

	  /**
	   * 查询促销列表接口
	   * @author henrysun
	   * 2018年5月7日 上午10:12:34
	   */
	  @GetMapping(value = "showAdsMiddleList")
	  @ResponseBody
	  @RequiresPermissions("ads:middle:list")
	  public ReType showAdsMiddleList(Model model, GoodsAdsMiddle goodsAdsMiddle, String page, String limit) {
	    return adsMiddleService.selectByPage(goodsAdsMiddle,Integer.valueOf(page),Integer.valueOf(limit));
	  }
	  
	  /**
	   * 跳转到添加商品页面
	   * @author henrysun
	   * 2018年5月7日 上午11:44:17
	   */
	  @GetMapping(value = "showAddGoodsSku")
	  public String showAddGoods(Model model) {
	    return "/goods/sku/add-goodsSku";
	  }

	
}

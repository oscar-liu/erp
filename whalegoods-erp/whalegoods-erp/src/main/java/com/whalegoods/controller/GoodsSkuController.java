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
import com.whalegoods.entity.GoodsSku;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.GoodsSkuService;
import com.whalegoods.util.FileUtil;
import com.whalegoods.util.JsonUtil;
import com.whalegoods.util.ReType;
import com.whalegoods.util.ShiroUtil;
import com.whalegoods.util.StringUtil;


/**
 * 商品基础SKU相关接口
 * @author henrysun
 * 2018年4月25日 下午8:03:13
 */
@Controller
@RequestMapping(value = "/goods")
public class GoodsSkuController  {
	
	  @Autowired
	  GoodsSkuService goodsSkuService; 
	
	  @Autowired
	  FileUtil fileUtil;

	  /**
	   * 跳转到商品SKU列表页面
	   * @author henrysun
	   * 2018年5月7日 上午10:12:39
	   */
	  @GetMapping(value = "showGoodsSku")
	  @RequiresPermissions("goods:sku:list")
	  public String showGoodsSku(Model model) {
	    return "/goods/sku/skuList";
	  }

	  /**
	   * 查询商品SKU列表接口
	   * @author henrysun
	   * 2018年5月7日 上午10:12:34
	   */
	  @GetMapping(value = "showGoodsSkuList")
	  @ResponseBody
	  @RequiresPermissions("goods:sku:list")
	  public ReType showGoodsSkuList(Model model, GoodsSku goodsSku, String page, String limit) {
	    return goodsSkuService.selectByPage(goodsSku,Integer.valueOf(page),Integer.valueOf(limit));
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

	  /**
	   * 添加商品接口
	   * @author henrysun
	   * 2018年5月7日 上午11:44:51
	   */
	  @PostMapping(value = "addGoodsSku")
	  @ResponseBody
	  public ResBody addGoodsSku(@RequestBody GoodsSku goodsSku) {
		  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		  goodsSku.setId(StringUtil.getUUID());
		  goodsSku.setCreateBy(ShiroUtil.getCurrentUserId());
		  goodsSku.setUpdateBy(ShiroUtil.getCurrentUserId());
		  goodsSku.setBoxCost(goodsSku.getOneCost()*goodsSku.getBoxNum());
		  goodsSku.setProfit(goodsSku.getOneCost()/goodsSku.getMarketPrice());
		  goodsSkuService.insert(goodsSku);
		  return resBody;
	  }

	  /**
	   * 跳转到更新商品页面
	   * @author henrysun
	   * 2018年5月7日 上午11:54:54
	   */
	  @GetMapping(value = "showUpdateGoodsSku")
	  public String showUpdateGoodsSku(String id, Model model) {
	    if (!StringUtil.isEmpty(id)) {
	      GoodsSku goodsSku=goodsSkuService.selectById(id);
	      model.addAttribute("goodsSku", goodsSku);
	    }
	    return "goods/sku/update-goodsSku";
	  }


	  /**
	   * 更新商品接口
	   * @author henrysun
	   * 2018年4月26日 下午3:30:49
	   */
	  @PostMapping(value = "updateGoodsSku")
	  @ResponseBody
	  public ResBody updateGoodsSku(@RequestBody GoodsSku goodsSku) {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		goodsSku.setUpdateBy(ShiroUtil.getCurrentUserId());
		goodsSku.setBoxCost(goodsSku.getOneCost()*goodsSku.getBoxNum());
		goodsSku.setProfit(goodsSku.getOneCost()/goodsSku.getMarketPrice());
		goodsSkuService.updateByObjCdt(goodsSku);
		return resBody;
	  }

	  /**
	   * 删除商品接口
	   * @author henrysun
	   * 2018年4月26日 下午3:32:39
	   */
	  @PostMapping(value = "/delGoodsSku")
	  @ResponseBody
	  @RequiresPermissions("goods:sku:del")
	  public ResBody delGoodsSku(String id) {
       ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
       goodsSkuService.deleteById(id);
	   return resBody;
	  }
	  
	  /**
	   * 上传商品图片接口
	   * @author henrysun
	   * 2018年5月7日 上午11:42:14
	   */
	  @PostMapping(value = "uploadGoodsPic")
	  @ResponseBody
	  public JsonUtil uploadGoodsPic(HttpServletRequest request,HttpSession session) {
		  JsonUtil jsonUtil=JsonUtil.sucess(null);
		  String childFolder="goods_pic";
		  String newFileName=childFolder+"_"+System.currentTimeMillis()+StringUtil.randomString(3);
		  String fileUrl=null;
		  try {
			  fileUrl= fileUtil.uploadFile(request,childFolder,newFileName);
			  JSONObject json=new JSONObject();
			  json.put("file_url",fileUrl);
			  jsonUtil.setData(json);
		} catch (SystemException e) {
			jsonUtil.setFlag(false);
			jsonUtil.setMsg("上传失败，请联系管理员");
		}
		  return jsonUtil;
	  }
	  
	  /**
	   * 验证商品编号是否存在
	   * @author henrysun
	   * 2018年4月26日 下午3:32:17
	   */
	  @GetMapping(value = "checkGoodsCode")
	  @ResponseBody
	  public ResBody checkGoodsCode(@RequestParam String goodsCode) {
		  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
	    if(goodsSkuService.checkGoodsCode(goodsCode)){
	    	resBody.setResultCode(ConstApiResCode.GOODS_CODE_EXIST);
	    	resBody.setResultMsg(ConstApiResCode.getResultMsg(ConstApiResCode.GOODS_CODE_EXIST));
	    }
	    return resBody;
	  }
	
}

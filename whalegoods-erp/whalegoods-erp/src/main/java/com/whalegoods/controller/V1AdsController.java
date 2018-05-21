package com.whalegoods.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.GoodsAdsMiddle;
import com.whalegoods.entity.request.ReqGetAdsMiddleList;
import com.whalegoods.entity.request.ReqGetAdsTopList;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.entity.response.ResGoodsAdsMiddle;
import com.whalegoods.entity.response.ResGoodsAdsTop;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.GoodsAdsMiddleService;
import com.whalegoods.service.GoodsAdsTopService;
import com.whalegoods.util.DateUtil;

/**
 * 广告促销API
 * @author henry-sun
 *
 */
@RestController
@RequestMapping(value = "/v1/ads")
public class V1AdsController {
	
	  @Autowired
	  GoodsAdsTopService goodsAdsTopService;
	  
	  @Autowired
	  GoodsAdsMiddleService goodsAdsMiddleService;

	  /**
	   * 获取广告列表接口
	   * @author henrysun
	   * 2018年5月15日 上午10:44:15
	   */
	  @GetMapping(value="/getTopList")
	  ResBody getlistGoodsAdsTop(@Valid ReqGetAdsTopList objCdt) {
		  List<ResGoodsAdsTop> dataList=goodsAdsTopService.selectAdsTopList(objCdt);
		  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS),dataList);
		  return resBody;
		}
	  
	  /**
	   * 获取促销商品列表接口
	   * @author henrysun
	   * 2018年5月15日 上午10:44:01
	   */
	  @GetMapping(value="/getMiddleList")
	  ResBody getlistGoodsAdsMiddle(@Valid ReqGetAdsMiddleList objCdt) throws SystemException  {
		  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		  List<GoodsAdsMiddle> adsMiddles=goodsAdsMiddleService.selectAdsMiddleList(objCdt);
		  Date nowDate=new Date();
		  List<ResGoodsAdsMiddle> resList=new ArrayList<>();
		  ResGoodsAdsMiddle item=null;
		  for (GoodsAdsMiddle adsMiddle : adsMiddles) {
			if(adsMiddle.getType()==1){
				 if(DateUtil.belongTime(nowDate,DateUtil.getFormatHms(adsMiddle.getStartHms(),nowDate), DateUtil.getFormatHms(adsMiddle.getEndHms(),nowDate))){
					 item=new ResGoodsAdsMiddle();
				  }
			}
			if(adsMiddle.getType()==2){
				 if(DateUtil.belongTime(nowDate,adsMiddle.getStartDate(),adsMiddle.getEndDate())){
					 item=new ResGoodsAdsMiddle();
				  }
			}
			BeanUtils.copyProperties(adsMiddle,item);
			resList.add(item);
		}
		  resBody.setData(resList);
		  return resBody;
		}
}

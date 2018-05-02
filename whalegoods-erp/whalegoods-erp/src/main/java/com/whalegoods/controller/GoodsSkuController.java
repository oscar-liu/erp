package com.whalegoods.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.request.ReqBase;
import com.whalegoods.entity.response.ResGoodsAdsTop;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.GoodsAdsMiddleService;
import com.whalegoods.service.GoodsAdsTopService;

/**
 * 商品基础SKU相关接口
 * @author henrysun
 * 2018年4月25日 下午8:03:13
 */
@RestController
@RequestMapping(value = "/goods")
public class GoodsSkuController  {

	
}

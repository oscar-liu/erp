package com.whalegoods.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * API返回状态编码和编码语义
 * @author henrysun
 * 2018年5月3日 下午3:56:01
 */
public abstract class ConstApiResCode {
	
	public static final Map<Integer, String> resMap = new HashMap<>(300);
	
    public final static Integer SUCCESS= 0;
    public final static Integer EXCEPTION_UNKNOWN= 10000;
    public final static Integer SYSTEM_ERROR= 10001;
    public final static Integer APK_VERSION_EXIST= 10002;
    public final static Integer ILLEGAL_CRON_EXPRESSION= 10003;
    
    
    /**
     * 促销相关 6开头
     */
    public final static Integer GOODS_CODE_FOR_SALE_EXIST= 6001;
    public final static Integer SALE_ALREADY_THREE= 6002;
    public final static Integer ADS_TOP_ALREADY_THREE= 6003;
    public final static Integer GOODS_CODE_FOR_ADS_TOP_EXIST= 6004;
    public final static Integer TIME_RANGE_NOT_EMPTY= 6005;
    
    /**
     * 商品库存相关 7开头
     */
    public final static Integer GOODS_CODE_NOT_EMPTY= 7001;
    public final static Integer PATH_CODE_NOT_EMPTY= 7002;
    public final static Integer VIEWTIME_NOT_EMPTY= 7003;
    public final static Integer STOCK_NOT_ENOUGH= 7004;
    public final static Integer GOODS_CODE_EXIST= 7005;
    public final static Integer GOODS_CODE_NOT_EXIST= 7006;
    
    /**
     * 设备和货道相关 8开头
     */
    public final static Integer PATH_NOT_EXIST= 8001;
    public final static Integer DEVICE_NOT_EXIST= 8002;
    public final static Integer PATH_EXIST= 8003;
    public final static Integer CAPACITY_CANNOT_BIGGER_THAN_WARNINGNUM= 8004;
    public final static Integer STANDARD_NOT_EXIST= 8005;
    public final static Integer ROADLIST_EMPTY= 8006;
    
    /**
     * 订单相关 9开头
     */
    public final static Integer WX_PREPAY_ONE_FAILED= 9001;
    public final static Integer WX_PREPAY_TWO_FAILED= 9002;
    public final static Integer WX_NOTIFY_ONE_FAILED= 9003;
    public final static Integer WX_NOTIFY_TWO_FAILED= 9004;
    public final static Integer ORDER_PREPAY_NOT_EXIST= 9005;
    public final static Integer WX_QUERY_ONE_FAILED= 9006;
    public final static Integer WX_QUERY_TWO_FAILED= 9007;
    /*public final static Integer WX_ORDER_FAILED= 9008;*/
    public final static Integer PAY_TYPE_ERROR= 9009;
    public final static Integer ALIPAY_ORDER_FAILED= 9010;
    public final static Integer WX_APPLY_REFUND_ONE_FAILED= 9011;
    public final static Integer WX_APPLY_REFUND_TWO_FAILED= 9012;
    public final static Integer ALIPAY_REFUND_FALID= 9013;
    public final static Integer ORDERID_EMPTY= 9014;
    public final static Integer CANNOT_TWELEVE= 9015;
    public final static Integer SDLIST_EMPTY= 9016;
    
    
	static {
		resMap.put(SUCCESS,"成功");
		resMap.put(EXCEPTION_UNKNOWN,"未知异常");
		resMap.put(SYSTEM_ERROR,"系统错误");
		resMap.put(APK_VERSION_EXIST,"APK版本已存在");
		resMap.put(ILLEGAL_CRON_EXPRESSION,"不合法的cron表达式");
		
		resMap.put(GOODS_CODE_FOR_SALE_EXIST,"商品已在对应的设备促销中");
		resMap.put(SALE_ALREADY_THREE,"该设备已经有三款商品在促销");
		resMap.put(ADS_TOP_ALREADY_THREE,"该设备已经有三个广告");
		resMap.put(GOODS_CODE_FOR_ADS_TOP_EXIST,"商品已在对应的设备投放广告");
		resMap.put(TIME_RANGE_NOT_EMPTY,"时间范围不能为空");
		
		resMap.put(GOODS_CODE_NOT_EMPTY,"商品编号不能为空");
		resMap.put(PATH_CODE_NOT_EMPTY,"货道编号不能为空");
		resMap.put(VIEWTIME_NOT_EMPTY,"限时折扣商品时间戳不能为空");
		resMap.put(STOCK_NOT_ENOUGH,"该商品所在货道库存不足");
		resMap.put(GOODS_CODE_EXIST,"商品编号已存在");
		resMap.put(GOODS_CODE_NOT_EXIST,"商品编号不存在");
		
		resMap.put(PATH_NOT_EXIST,"该货道不存在");
		resMap.put(DEVICE_NOT_EXIST,"设备不存在");
		resMap.put(PATH_EXIST,"货道已存在");
		resMap.put(CAPACITY_CANNOT_BIGGER_THAN_WARNINGNUM,"临界值不能大于或等于货道容量值");
		resMap.put(STANDARD_NOT_EXIST,"型号标准不存在");
		resMap.put(ROADLIST_EMPTY,"货道清单为空");
		
		resMap.put(WX_PREPAY_ONE_FAILED,"微信预支付-一级失败");
		resMap.put(WX_PREPAY_TWO_FAILED,"微信预支付-二级失败");
		resMap.put(WX_NOTIFY_ONE_FAILED,"微信支付结果通知-一级失败");
		resMap.put(WX_NOTIFY_TWO_FAILED,"微信支付结果通知-二级失败");
		resMap.put(ORDER_PREPAY_NOT_EXIST,"预支付订单不存在");
		resMap.put(WX_QUERY_ONE_FAILED,"微信查询订单-一级失败");
		resMap.put(WX_QUERY_TWO_FAILED,"微信查询订单-二级失败");
		/*resMap.put(WX_ORDER_FAILED,"微信订单交易失败");*/
		resMap.put(PAY_TYPE_ERROR,"支付类型错误");
		resMap.put(ALIPAY_ORDER_FAILED,"支付宝订单交易失败");
		resMap.put(WX_APPLY_REFUND_ONE_FAILED,"微信申请退款-一级失败");
		resMap.put(WX_APPLY_REFUND_TWO_FAILED,"微信申请退款-二级失败");
		resMap.put(ALIPAY_REFUND_FALID,"支付宝退款申请失败");
		resMap.put(ORDERID_EMPTY,"订单号不能为空");
		resMap.put(CANNOT_TWELEVE,"不能超过12个月");
		resMap.put(SDLIST_EMPTY,"当前用户无刷单记录");
		
	}
	
	public static String getResultMsg(Integer resultCode) {
		return resMap.get(resultCode);
	}
}

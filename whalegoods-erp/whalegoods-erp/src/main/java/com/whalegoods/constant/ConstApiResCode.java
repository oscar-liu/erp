package com.whalegoods.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * API返回状态编码和编码语义
 * @author chencong
 *
 */
public abstract class ConstApiResCode {
	
	public static final Map<Integer, String> resMap = new HashMap<>(300);
	
    public final static Integer SUCCESS= 0;
    public final static Integer EXCEPTION_UNKNOWN= 10000;
    public final static Integer SYSTEM_ERROR= 10001;
    
    /**
     * 库存相关 6开头
     */
    public final static Integer 发是非得失= 6001;
    
    /**
     * 库存相关 7开头
     */
    public final static Integer 发送到发送到= 7001;
    
    /**
     * 设备和货道相关 8开头
     */
    public final static Integer PATH_NOT_EXIST= 8001;
    
    /**
     * 订单相关 9开头
     */
    public final static Integer WX_PREPAY_ONE_FAILD= 9001;
    public final static Integer WX_PREPAY_TWO_FAILD= 9002;
    public final static Integer WX_NOTIFY_ONE_FAILD= 9003;
    public final static Integer WX_NOTIFY_TWO_FAILD= 9004;
    public final static Integer ORDER_PREPAY_NOT_EXIST= 9005;
    public final static Integer WX_QUERY_ONE_FAILD= 9006;
    public final static Integer WX_QUERY_TWO_FAILD= 9007;
    /*public final static Integer WX_ORDER_FAILD= 9008;*/
    public final static Integer PAY_TYPE_ERROR= 9009;
    public final static Integer ALIPAY_ORDER_FAILD= 9010;
    
    
	static {
		resMap.put(SUCCESS,"成功");
		resMap.put(EXCEPTION_UNKNOWN,"未知异常");
		resMap.put(SYSTEM_ERROR,"系统错误");
		
		resMap.put(PATH_NOT_EXIST,"该货道不存在");
		
		resMap.put(WX_PREPAY_ONE_FAILD,"微信预支付-一级失败");
		resMap.put(WX_PREPAY_TWO_FAILD,"微信预支付-二级失败");
		resMap.put(WX_NOTIFY_ONE_FAILD,"微信支付结果通知-一级失败");
		resMap.put(WX_NOTIFY_TWO_FAILD,"微信支付结果通知-二级失败");
		resMap.put(ORDER_PREPAY_NOT_EXIST,"预支付订单不存在");
		resMap.put(WX_QUERY_ONE_FAILD,"微信查询订单-一级失败");
		resMap.put(WX_QUERY_TWO_FAILD,"微信查询订单-二级失败");
		/*resMap.put(WX_ORDER_FAILD,"微信订单交易失败");*/
		resMap.put(PAY_TYPE_ERROR,"支付类型错误");
		resMap.put(ALIPAY_ORDER_FAILD,"支付宝订单交易失败");
		
	}
	
	public static String getResultMsg(Integer resultCode) {
		return resMap.get(resultCode);
	}
}

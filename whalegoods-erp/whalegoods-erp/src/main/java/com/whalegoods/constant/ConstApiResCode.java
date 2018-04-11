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
    public final static Integer WX_NOTIFY_TWO_FAILD= 9003;
    
    
	static {
		resMap.put(SUCCESS,"成功");
		resMap.put(EXCEPTION_UNKNOWN,"未知异常");
		resMap.put(SYSTEM_ERROR,"系统错误");
		
		resMap.put(PATH_NOT_EXIST,"该货道不存在");
		
		resMap.put(WX_PREPAY_ONE_FAILD,"微信预支付-一级失败");
		resMap.put(WX_PREPAY_TWO_FAILD,"微信预支付-二级失败");
		resMap.put(WX_NOTIFY_ONE_FAILD,"微信支付结果通知-一级失败");
		resMap.put(WX_NOTIFY_TWO_FAILD,"微信支付结果通知-二级失败");
		
	}
	
	public static String getResultMsg(Integer resultCode) {
		return resMap.get(resultCode);
	}
}

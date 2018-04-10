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
    
    /**
     * 订单相关 9开头
     */
    public final static Integer PREPAY_FAILD= 9001;
    
    
	static {
		resMap.put(SUCCESS,"成功");
		
		resMap.put(PREPAY_FAILD,"微信预支付-通信失败");
	}
	
	public static String getResultMsg(Integer resultCode) {
		return resMap.get(resultCode);
	}
}

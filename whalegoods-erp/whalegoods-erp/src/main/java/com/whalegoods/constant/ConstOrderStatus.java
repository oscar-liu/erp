package com.whalegoods.constant;

/**
 * 订单状态常量
 * @author henrysun
 * 2018年5月10日 上午11:17:12
 */
public abstract class ConstOrderStatus {

    public final static Byte NOT_PAY= 1;
    public final static Byte PAID= 2;
    public final static Byte PAY_FAILED= 3;
    public final static Byte APPLY_REFUND_SUCCESS= 4;
    public final static Byte CLOSED= 5;
    
}

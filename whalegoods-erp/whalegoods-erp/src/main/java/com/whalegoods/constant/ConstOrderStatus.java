package com.whalegoods.constant;

/**
 * 订单状态常量
 * @author chencong
 *
 */
public abstract class ConstOrderStatus {

    public final static Byte NOT_PAY= 1;
    public final static Byte PAID= 2;
    public final static Byte PAY_FAILED= 3;
    public final static Byte REFUND= 4;
    
}

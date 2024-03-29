package com.whalegoods.constant;

public class ConstSysParamValue {
	
	//微信API根地址
	public static String WX_API="https://api.mch.weixin.qq.com";
	//微信统一下单API地址
	public static String WX_PREPAY_URL=WX_API+"/pay/unifiedorder";
	//微信关闭订单API地址
	public static String WX_CLOSE_ORDER_URL=WX_API+"/pay/closeorder";
	//支付宝统一网关地址
	public static String ALIPAY_URL="https://openapi.alipay.com/gateway.do";
	//微信公众账号ID
	public static String WX_APPID="wxa2e9d7a75fbfd27c";
	//支付宝应用ID
	public static String ALIPAY_APPID="2018011901976775";
	//微信商户号
	public static String WX_MCHID="1496138162";
	//微信支付结果通知地址
	public static String WX_NOTIFY_URL="1496138162";
	//私钥
	public static String WX_KEY="whalegoods1811181234567891012345";
	//支付宝私钥
	public static String ALIPAY_PRIVATE_KEY="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDnwIqnm7gUwjkRa5SjIgKaH/q8fJ8AFIDlLIqcJrT48BAtMfjuIC6CMtuQRQtvPH/rEde8p7z8iFIIcCip2obKaHoz7daw/ixXnTVPCiAOfd65L22/Rc7LwpOLZdr/bOkPiI65rxGnOjo0UZj+cEorlNnTsMAvrSf1w9mMFh20DY7XJO4vmwk2Z0lvXBts8N0KqSXaxeR2bJvYuxyyqhsrmSCwbMjWFkD+1UvBZ8nNyO8r/gV1t9oZjNHpMi3JgopjOSpTWrbnAAW6lRV8Ybp0hwCPfInKPgN2EoUAxXwltAtrHur5x2s3uKF9miykKbE8tJYq6qnkH6r5Lh8UcY61AgMBAAECggEAZv3tYMFWVHtgKzq5Kqphp6cQohVr//gctDxQOUcGZB8jwUIVE5ZftOLvKrVUeJHVc11Rl/ifh0b51eAqYhnIj0lRDaTkJXI/uIwX9dFVH7uWcIwAZOGcHneRZIDWi28WQB3699AyszOwTDH7IRA8FhQnnelN76IvQQxRsOzfNHV9Y1neN+Grhpy8pL8nffeqGF+gzWlbw+t1dt855wyGmUMfb9xY0ENTFjNoBY1SEXoThdU4T4D3kZ7jVLrX80lVoZevaG+BkKfHtAjWHIvehzmsBYUcPrOAAkTHR7YFX3WDTUSuiR+Xd7XpPTIEgXiu7hbtUyzMYq9WluOSn5yuXQKBgQD8jg+8tLGTPdeksOeqX1u6At9eYwmVFVzQergCpWDxXbkEndxrr97RMDKchnGBSBOp5YA2VkBOAAjqoZOIgifdfMKU5dTol5rk1L3aztcXrvRSqYrJx0IzGXju+5RwvLpB7B9MixGRKt6BdNHX3DldH4TtizRnhK/YVw9WrbFOuwKBgQDq6dXZhhfraprrIKw4DQBGIJqcslpJKZICq0s7ZckXeIMnBk3fZgLPSzmjB9RQfw/SdNC/Vsuy/2okKRvv0rHetA0vuJ0kfeF25dqD6QoId79I66OPFwACV4w4iZsS8EkOAUMSoA37GwkJRJ7dLx6RUZUz01CweiRwngpXnn4ZTwKBgEUsK8dFvnVTtOKYyXryj08NRly1x4Qby8YWNpwS69BwGSLYkey0yNSG3PXeJOyB71DTWSCI0ygJ/mc1RhFXhM+3QhxCLuRd0cDnO/DYQjVsZ+LUKP1HJCyWx3a9VVTjfCheIdx3IqKeGT9f3bG/0b5dyay+OeY4g2k/vyXAgRXNAoGANKt1HS5pkoj2bx2GP5L6qr6Iq/V5NMozSJHrbkfmDHMixM8vCpWur5eoNpt8yQeUHpW+cAgf5+RIgg8NinntE4e/JSH5acMnmCb1FXpcm4VtyAW9SEnZ4KyMbei8ckSZvKAfz78/IzvujLtr+x+jdOWb1GDeyJK6NN3aZ8bLcxUCgYEAhwFPGcY/joQAb2zFvMQvyn5XKPQkXCyBCe1AP/kue7lhB7GNieVNZr7+9pmQFc3Lay9g2CoXNdoXzF06qodqX3dDpDWJC7Ki0OjGGg4U8pmjgJHYCTme6xDgybqlIxDFcfac1PehC5fDksj5uEUXwfNBB/HW4Tgbdsw7VfP4mHY=";
	//支付宝公钥
	public static String ALIPAY_PUBLIC_KEY="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzg1zUdBaKkhxyhyY2pU++6anP5GHvST3uRmoY3FrNIrEbHmGnhWWbRokwMz6hLDecuHJ7556NlF4HZU+f+IC5uNBjK8W7Dcm1iZahix0UgeNWrVjZ1PMlvS9g4W8UMvmCDDlKtOnENxaYa0BRaiLi0PXreD2MeoKV9ZYhyzfsLv9zOfWagQwNIcwJH+bkop1sIXTPDQbc1+cnbIlQOKT+rlktOfTh5HVLlwT/gGaIj0E3UqH/654BuZvvfIVd4DLs0L3hfixAazV5URRnZSjp+xEIRmE9OXBTE+xyBLkc5gCeWY+xm+7Pq3A7T83Fgc7vw0pXxXAsumNcmafnXP2nwIDAQAB";
	//微信查询地址
	public static String WX_QUERY=WX_API+"/pay/orderquery";
	//微信申请退款地址
	public static String WX_APPLY_REFUND=WX_API+"/secapi/pay/refund";
	//判断设备是否下线间隔时间
	public static Long DEVICE_OFFLINE_TIME=5*60*1000L;
	//倒退十分钟
	public static Long BACK_TEN_MIN=10*60*1000L;
	//倒退四十分钟
	public static Long BACK_FOURTY_MIN=40*60*1000L;
	
}

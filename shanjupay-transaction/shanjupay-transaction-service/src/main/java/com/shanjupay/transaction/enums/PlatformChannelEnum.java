package com.shanjupay.transaction.enums;

public enum PlatformChannelEnum {

    SHANJU_B2C("shanju_b2c", "闪聚b扫c"),

    SHANJU_C2B("shanju_c2b", "闪聚c扫b"),

    WX_NATIVE("wx_native", "微信native支付"),

    ALIPAY_WAP("alipay_wap", "支付宝手机网站支付"),
    ;

    private String platformChannelCode;
    private String platformChannelName;

    public String getPlatformChannelName() {
        return platformChannelName;
    }

    public void setPlatformChannelName(String platformChannelName) {
        this.platformChannelName = platformChannelName;
    }

    public String getPlatformChannelCode() {
        return platformChannelCode;
    }

    public void setPlatformChannelCode(String platformChannelCode) {
        this.platformChannelCode = platformChannelCode;
    }

    PlatformChannelEnum(String platformChannelCode, String platformChannelName) {
        this.platformChannelCode = platformChannelCode;
        this.platformChannelName = platformChannelName;
    }
}

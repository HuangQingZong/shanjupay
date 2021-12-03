package com.shanjupay.merchant.service;

public interface SmsService {

    /**
     * 获取短信验证码
     * @param phone 手机号
     * @return 验证码
     */
    String sendVerifyCode(String phone);

    /**
     * 校验验证码，抛出异常则验证码无效
     * @param verifyKey
     * @param verifyCode
     */
    void checkVerifyCode(String verifyKey, String verifyCode);
}

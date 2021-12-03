package com.shanjupay.merchant.service.impl;

import com.alibaba.fastjson.JSON;
import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.merchant.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service  //实例为一个bean
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Value("${sms.url}")
    String smsUrl;

    @Value("${sms.effectiveTime}")
    String effectiveTime;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public String sendVerifyCode(String phone) {

        String generateCodeUrl = smsUrl + "/generate?name=sms&effectiveTime=" + effectiveTime;
        //=>http://localhost:56085/sailing/generate?name=sms&effectiveTime=1200

        //------------ 组装请求消息 ------------
        //请求体
        HashMap<String, Object> requestParamsMap = new HashMap<>();
        requestParamsMap.put("mobile", phone);
        //请求头
        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.setContentType(MediaType.APPLICATION_JSON); //请求参数为json格式
        //请求消息
        HttpEntity requestEntity = new HttpEntity(requestParamsMap, requestHeader);
        //=>{"body":{"mobile":"11122223333"},"headers":{"Content-Type":["application/json"]}}

        //------------ 发送请求 ------------
        ResponseEntity<Map> responseEntity = null;
        try {
            //响应消息
            responseEntity = restTemplate.exchange(generateCodeUrl, HttpMethod.POST, requestEntity, Map.class);
            //=>{"body":{"code":0,"msg":"正常","result":{"key":"sms:6432b54ed0a04e1b85b755099adb5b2f","content":"184588"}},"headers":{"Content-Type":["application/json;charset=UTF-8"]},"statusCode":"OK","statusCodeValue":200}
            log.info("请求短信服务发送验证码, url: 【{}】, 请求消息: 【{}】, 响应消息: 【{}】", generateCodeUrl, JSON.toJSONString(requestEntity), JSON.toJSONString(responseEntity));
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("发送验证码错误");
        }

        //------------ 解析响应 ------------
        //响应体
        Map responseResultMap = responseEntity.getBody();
        //=>{"code":0,"msg":"正常","result":{"key":"sms:6432b54ed0a04e1b85b755099adb5b2f","content":"184588"}}
        if (responseResultMap == null || responseResultMap.get("result") == null) {
            throw new RuntimeException("发送验证码错误");
        }

        Map resultMap = (Map) responseResultMap.get("result");
        //=>{"key":"sms:6432b54ed0a04e1b85b755099adb5b2f","content":"184588"}
        String verifyKey = resultMap.get("key").toString(); //验证码key
//        String verifyCode = resultMap.get("content").toString(); //短信验证码

        return verifyKey;
    }

    @Override
    public void checkVerifyCode(String verifyKey, String verifyCode) {

        String verifyCodeUrl = smsUrl + "/verify?name=sms&verificationKey=" + verifyKey
                 + "&verificationCode=" + verifyCode;
        //=>http://localhost:56085/sailing/verify?name=sms&verificationKey=sms:7736f3ad8c674b0095746739f492eccd&verificationCode=020015

        //------------ 发送请求 ------------
        ResponseEntity<Map> responseEntity = null;
        try {
            //响应消息
            responseEntity = restTemplate.exchange(verifyCodeUrl, HttpMethod.POST, HttpEntity.EMPTY/*请求体为空*/, Map.class);
            //=>{"body":{"code":0,"msg":"正常","result":true},"headers":{"Content-Type":["application/json;charset=UTF-8"]},"statusCode":"OK","statusCodeValue":200}
            log.info("请求短信服务校验验证码, url: 【{}】, 请求消息: 【】, 响应消息: 【{}】", verifyCodeUrl, JSON.toJSONString(responseEntity));
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
//            throw new RuntimeException("验证码错误");
            throw new BusinessException(CommonErrorCode.E_100102); //抛出自定义异常类型
        }

        //------------ 解析响应 ------------
        //响应体
        Map responseResultMap = responseEntity.getBody();
        //=>"body":{"code":0,"msg":"正常","result":true}
        if (responseResultMap == null || "false".equals(responseResultMap.get("result").toString())) {
//            throw new RuntimeException("验证码错误");
            throw new BusinessException(CommonErrorCode.E_100102); //抛出自定义异常类型
        }
    }


}
















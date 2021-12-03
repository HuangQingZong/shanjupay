package com.shanjupay.merchant;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RestTemplateTest {

    @Autowired
    RestTemplate restTemplate;

    @Test //================ 测试restTemplate用法 ================
    public void testRestTemplate(){
        String url = "http://www.baidu.com/";
        //发送请求, 获取响应
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        String body = forEntity.getBody();
        System.out.println(body);
    }

    @Test //================ 获取验证码 ================
    public void testGetSmsCode(){
        String url ="http://localhost:56085/sailing/generate?name=sms&effectiveTime=600";
        String mobile = "11122223333";

        //------------ 组装请求消息 ------------
        //请求体
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("mobile", mobile);
        //请求头
        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.setContentType(MediaType.APPLICATION_JSON); //请求参数为json格式
        //请求消息
        HttpEntity requestEntity = new HttpEntity(requestMap, requestHeader);
        //=>{"body":{"mobile":"11122223333"},"headers":{"Content-Type":["application/json"]}}

        //------------ 发送请求 ------------
        ResponseEntity<Map> responseEntity = null;
        Map responseMap = null;
        try {
            int i = 1 / 0; //制造异常
            //响应消息
            responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);
            //=>{"body":{"code":0,"msg":"正常","result":{"key":"sms:6432b54ed0a04e1b85b755099adb5b2f","content":"184588"}},"headers":{"Content-Type":["application/json;charset=UTF-8"]},"statusCode":"OK","statusCodeValue":200}
            log.info("调用短信微服务发送验证码, url: 【{}】, 请求消息: 【{}】, 响应消息: 【{}】", url, JSON.toJSONString(requestEntity), JSON.toJSONString(responseEntity));
        } catch (RestClientException e) {
            e.printStackTrace();
            log.error("异常信息: " + e.getMessage(), e);
        }

        //------------ 解析响应 ------------
        //响应体
        responseMap = responseEntity.getBody();
        //=>{"code":0,"msg":"正常","result":{"key":"sms:6432b54ed0a04e1b85b755099adb5b2f","content":"184588"}}
        if (responseMap != null && responseMap.get("result") != null) {
            Map resultMap = (Map) responseMap.get("result");
            //=>{"key":"sms:6432b54ed0a04e1b85b755099adb5b2f","content":"184588"}
            String key = resultMap.get("key").toString(); //key
            String verifyCode = resultMap.get("content").toString(); //短信验证码
        }
    }

    @Test //================ 测试String.format()用法 ================
    public void testStringFormat(){
        String generateCodeUrl = String.format("%s/generate?name=sms&effectiveTime=%s", "sms", 600);
        System.out.println(generateCodeUrl);
    }

}












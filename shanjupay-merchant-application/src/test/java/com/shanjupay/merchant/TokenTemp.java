package com.shanjupay.merchant;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shanjupay.common.util.EncryptUtil;
import com.shanjupay.merchant.api.MerchantService;
import com.shanjupay.merchant.dto.MerchantDTO;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TokenTemp {

    @Reference //注入远程的bean
    MerchantService merchantService;

    //生成token，指定商户id
    @Test
    public void createToken() {
        Long merchantId = 1462716677618671617L; //资质申请的商户id
        MerchantDTO merchantDTO = merchantService.queryMerchantById(merchantId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("merchantId", merchantId);
        jsonObject.put("username", merchantDTO.getUsername());
        jsonObject.put("mobile", merchantDTO.getMobile());
        //json对象先转字节数组，再base64编码
        String token = "Bearer " + EncryptUtil.encodeBase64(JSON.toJSONString(jsonObject).getBytes());
        System.out.println(token);
        //=>Bearer eyJtZXJjaGFudElkIjoxNDYyNzE2Njc3NjE4NjcxNjE3LCJtb2JpbGUiOiIxMTEyMjIyMzMzMyIsInVzZXJuYW1lIjoi5byg5YWI55SfIn0=
    }
}

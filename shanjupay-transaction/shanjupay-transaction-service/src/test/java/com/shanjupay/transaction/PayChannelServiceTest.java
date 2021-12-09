package com.shanjupay.transaction;

import com.alibaba.fastjson.JSON;
import com.shanjupay.transaction.api.PayChannelService;
import com.shanjupay.transaction.dto.PayChannelDTO;
import com.shanjupay.transaction.dto.PayChannelParamDTO;
import com.shanjupay.transaction.dto.PlatformChannelDTO;
import com.shanjupay.transaction.enums.PlatformChannelEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class PayChannelServiceTest {

    @Autowired
    PayChannelService payChannelService;

    @Test
    //==================== 查询平台服务类型 ====================
    public void testQueryPlatformChannel(){
        List<PlatformChannelDTO> platformChannelDTOList = payChannelService.queryPlatformChannel();
        System.out.println(platformChannelDTOList);
        //=>[PlatformChannelDTO(id=1, channelName=闪聚b扫c, channelCode=shanju_b2c),
        // PlatformChannelDTO(id=2, channelName=闪聚c扫b, channelCode=shanju_c2b),
        // PlatformChannelDTO(id=3, channelName=微信native支付, channelCode=wx_native),
        // PlatformChannelDTO(id=4, channelName=支付宝手机网站支付, channelCode=alipay_wap)]
    }

    @Test
    //==================== 根据服务类型查询支付渠道====================
    public void testFindPayChannelByPlatformChannel(){
        List<PayChannelDTO> payChannelList = payChannelService.findPayChannelByPlatformChannel(PlatformChannelEnum.SHANJU_C2B.getPlatformChannelCode());
        System.out.println(payChannelList);
        //=>[PayChannelDTO(id=1, channelName=微信jsapi, channelCode=wx_jsapi),
        // PayChannelDTO(id=2, channelName=支付宝手机网站支付, channelCode=alipay_wap)]
    }

    @Test
    //==================== 保存支付渠道参数 ====================
    public void testSavePayChannelParam(){
        String paramJson = "{\n" +
                "    \"appId\": \"bc27fc62-4bb3-4bf4-b826-d3d4d3423a12\",\n" +
                "    \"platformChannelCode\": \"shanju_c2b\",\n" +
                "    \"payChannel\": \"wx_jsapi\",\n" +
                "    \"channelName\": \"微信jsapi接口配置4\", \n" +
                "    \"param\": \"{\\\"appID\\\":\\\"wxd2bf2dba2e86a8c7\\\",\\\"appSecret\\\":\\\"cec1a9185ad435abe1bced4b93f7ef2e\\\",\\\"key\\\":\\\"95fe355daca50f1ae82f0865c2ce87c8\\\",\\\"mchID\\\":\\\"1502570431\\\",\\\"payKey\\\":\\\"95fe355daca50f1ae82f0865c2ce87c8\\\"}\"\n" +
                "}";
        PayChannelParamDTO payChannelParamDTO = JSON.parseObject(paramJson, PayChannelParamDTO.class);
        payChannelParamDTO.setMerchantId(1462716677618671617L);
        payChannelService.savePayChannelParam(payChannelParamDTO);
        System.out.println();
    }

    @Test
    //==================== 查询支付渠道参数 ====================
    public void testFindPayChannelParamList(){
        String appId = "bc27fc62-4bb3-4bf4-b826-d3d4d3423a12";
        String platformChannelCode = "shanju_c2b";
        List<PayChannelParamDTO> payChannelParamDTOList = payChannelService.findPayChannelParamList(appId, platformChannelCode);
        System.out.println(payChannelParamDTOList);
    }

}

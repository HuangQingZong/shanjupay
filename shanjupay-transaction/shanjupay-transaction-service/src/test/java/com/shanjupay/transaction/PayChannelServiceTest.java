package com.shanjupay.transaction;

import com.shanjupay.transaction.api.PayChannelService;
import com.shanjupay.transaction.dto.PayChannelDTO;
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

}

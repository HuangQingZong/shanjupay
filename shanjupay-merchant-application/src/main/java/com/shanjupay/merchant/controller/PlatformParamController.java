package com.shanjupay.merchant.controller;

import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.merchant.common.util.SecurityUtil;
import com.shanjupay.transaction.api.PayChannelService;
import com.shanjupay.transaction.dto.PayChannelDTO;
import com.shanjupay.transaction.dto.PayChannelParamDTO;
import com.shanjupay.transaction.dto.PlatformChannelDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(value = "商户平台-渠道和支付参数相关", tags = "商户平台-渠道和支付参数", description = "商户平台-渠道和支付参数相关")
@Slf4j
@RestController
@RequestMapping("/platformParam")
public class PlatformParamController {

    @Reference
    PayChannelService payChannelService;

    @ApiOperation("获取平台服务类型")
    @GetMapping(value = "/queryPlatformChannel")
    public List<PlatformChannelDTO> queryPlatformChannel() {
        return payChannelService.queryPlatformChannel();
    }

    @ApiOperation("为应用绑定服务类型")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "应用id", name = "appId", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(value = "服务类型code", name = "platformChannelCode", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/bindPlatformChannelForApp/{appId}")
    void bindPlatformChannelForApp(@PathVariable("appId") String appId, @RequestParam("platformChannelCode") String platformChannelCode) {
        payChannelService.bindPlatformChannelForApp(appId, platformChannelCode);
    }

    @ApiOperation("查询应用是否绑定了某个服务类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用appId", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "platformChannel", value = "服务类型", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("/queryAppBindPlatformChannel")
    public boolean queryAppBindPlatformChannel(@RequestParam String appId, @RequestParam String platformChannel) {
        return payChannelService.queryAppBindPlatformChannel(appId, platformChannel);
    }

    @ApiOperation("根据服务类型查询支付渠道")
    @ApiImplicitParam(name = "platformChannelCode", value = "服务类型代码", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/findPayChannelByPlatformChannel/{platformChannelCode}")
    List<PayChannelDTO> findPayChannelByPlatformChannel(@PathVariable("platformChannelCode") String platformChannelCode) throws BusinessException {
        return payChannelService.findPayChannelByPlatformChannel(platformChannelCode);
    }

    @ApiOperation("商户配置支付渠道参数")
    @ApiImplicitParam(name = "payChannelParamDTO", value = "支付渠道参数", required = true, dataType = "PayChannelParamDTO", paramType = "body")
    @RequestMapping(value = "/savePayChannelParam", method = {RequestMethod.POST, RequestMethod.PUT})
    void savePayChannelParam(@RequestBody PayChannelParamDTO payChannelParamDTO) {
        if (payChannelParamDTO == null || payChannelParamDTO.getChannelName() == null) {
            throw new BusinessException(CommonErrorCode.E_300009);
        }
        Long merchantId = SecurityUtil.getMerchantId();
        payChannelParamDTO.setMerchantId(merchantId);
        payChannelService.savePayChannelParam(payChannelParamDTO);
    }

    @ApiOperation("根据应用、服务类型获取支付渠道参数列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "应用id", name = "appId", dataType = "String", paramType = "path"),
            @ApiImplicitParam(value = "服务类型代码", name = "platformChannel", dataType = "String", paramType = "path")
    })
    @GetMapping(value = "/findPayChannelParamList/{appId}/{platformChannel}")
    public List<PayChannelParamDTO> findPayChannelParamList(@PathVariable("appId") String appId, @PathVariable("platformChannel") String platformChannel) {
        return payChannelService.findPayChannelParamList(appId, platformChannel);
    }

    @ApiOperation("根据应用、服务类型和支付渠道获取单个支付渠道参数")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "应用id", name = "appId", dataType = "String", paramType = "path"),
            @ApiImplicitParam(value = "服务类型代码", name = "platformChannel", dataType = "String", paramType = "path"),
            @ApiImplicitParam(value = "支付渠道代码", name = "payChannel", dataType = "String", paramType = "path")
    })
    @GetMapping(value = "/findPayChannelParam/{appId}/{platformChannel}/{payChannel}")
    public PayChannelParamDTO findPayChannelParam(@PathVariable("appId") String appId, @PathVariable("platformChannel") String platformChannel, @PathVariable("payChannel") String payChannel) {
        return payChannelService.findPayChannelParam(appId, platformChannel, payChannel);
    }
}

package com.shanjupay.merchant.controller;

import com.shanjupay.merchant.api.AppService;
import com.shanjupay.merchant.common.util.SecurityUtil;
import com.shanjupay.merchant.dto.AppDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "商户平台-应用管理", tags = "商户平台-应用相关", description = "商户平台-应用相关")
@RestController
@RequestMapping("/app")
public class AppController {

    @Reference
    AppService appService;

    @ApiOperation("商户创建应用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "app", value = "应用信息", required = true, dataType = "AppDTO", paramType = "body")})
    @PostMapping(value = "/appCreate")
    public AppDTO createApp(@RequestBody AppDTO app) {
        //解析token，取出当前登录商户的id
        Long merchantId = SecurityUtil.getMerchantId();
        return appService.createApp(merchantId, app);
    }

    @ApiOperation("查询商户下的应用列表")
    @GetMapping(value = "/findAppList")
    public List<AppDTO> findAppList() {
        Long merchantId = SecurityUtil.getMerchantId();
        return appService.findAppListByMerchantId(merchantId);
    }

    @ApiOperation("根据应用id查询应用信息")
    @ApiImplicitParam(value = "应用id", name = "appId", dataType = "String", paramType = "path")
    @GetMapping(value = "/findApp/{appId}")
    public AppDTO findApp(@PathVariable("appId") String appId) {
        return appService.findAppByAppId(appId);
    }

}

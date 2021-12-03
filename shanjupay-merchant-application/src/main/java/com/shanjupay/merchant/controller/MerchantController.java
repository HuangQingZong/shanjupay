package com.shanjupay.merchant.controller;

import cn.hutool.core.bean.BeanUtil;
import com.shanjupay.merchant.api.MerchantService;
import com.shanjupay.merchant.api.dto.MerchantDTO;
import com.shanjupay.merchant.common.util.SecurityUtil;
import com.shanjupay.merchant.service.FileService;
import com.shanjupay.merchant.service.SmsService;
import com.shanjupay.merchant.valid.MerchantLoginGroup;
import com.shanjupay.merchant.vo.MerchantDetailVO;
import com.shanjupay.merchant.vo.MerchantRegisterVO;
import io.swagger.annotations.*;
import org.apache.dubbo.config.annotation.Reference;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * 商户
 **/
@RestController
@Api(value = "value: 商户平台应用接口", tags = "tags: 商户平台应用接口", description = "description: 商户平台应用接口")
@RequestMapping("/merchant")
//url: http://localhost:57010/merchant-application/merchant
public class MerchantController {

    @Reference //注入远程的bean
    MerchantService merchantService;

    @Autowired //注入本地的bean
    SmsService smsService;

    @Autowired
    FileService fileService;

    @ApiOperation(value = "根据id查询商户信息")
    @GetMapping("/{id}")  //url：/1
    //==================== 根据id查询商户信息 ====================
    public MerchantDTO queryMerchantById(@PathVariable("id") Long id) {
        MerchantDTO merchantDto = merchantService.queryMerchantById(id);
        return merchantDto;
    }

    @ApiOperation("获取手机验证码")
    @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String", paramType = "query")
    @GetMapping("/getVerifyCode")  //url: getVerifyCode?phone=11122223333
    //==================== 获取手机验证码 ====================
    public String getVerifyCode(@RequestParam String phone) {
        return smsService.sendVerifyCode(phone);
    }

    @ApiOperation("商户注册")
    @ApiImplicitParam(name = "merchantRegister", value = "注册信息", required = true, dataType = "MerchantRegisterVO", paramType = "body")
    @PostMapping("/register")
    //==================== 商户注册 ====================
    public MerchantRegisterVO merchantRegister(@RequestBody @Validated MerchantRegisterVO merchantRegisterVO) {
        //校验验证码
//        smsService.checkVerifyCode(merchantRegisterVO.getVerifyKey(), merchantRegisterVO.getVerifyCode());
        //vo转dto，使用hutool工具类
        MerchantDTO merchantDTO = BeanUtil.copyProperties(merchantRegisterVO, MerchantDTO.class);
        //商户注册
        merchantService.merchantRegister(merchantDTO);
        //注册成功则返回注册商户信息，异常终止则注册失败
        return merchantRegisterVO;
    }

    @ApiOperation("商户注册测试")
    @ApiImplicitParam(name = "merchantRegister", value = "注册信息", required = true, dataType = "MerchantRegisterVO", paramType = "body")
    @PostMapping("/merchantRegisterTest")
    //==================== 商户注册测试 ====================
    public MerchantRegisterVO merchantRegisterTest(@RequestBody @Validated(MerchantLoginGroup.class) MerchantRegisterVO merchantRegisterVO) { /*@Validated可以指定验证的分组*/
        int i = 1 / 0; //制造异常，测试全局异常处理

        //校验验证码
        smsService.checkVerifyCode(merchantRegisterVO.getVerifyKey(), merchantRegisterVO.getVerifyCode());

        //vo转dto
        //使用hutool工具类
        MerchantDTO merchantDTO = BeanUtil.copyProperties(merchantRegisterVO, MerchantDTO.class);
//        //使用mapstruct
//        MerchantDTO merchantDTO = MerchantRegisterConvert.INSTANCE.vo2Dto(merchantRegisterVO);
//        //原始方法
//        MerchantDTO merchantDTO = new MerchantDTO()
//                .setUsername(merchantRegisterVo.getUsername())
//                .setMobile(merchantRegisterVo.getMobile());

        //商户注册
        merchantService.merchantRegister(merchantDTO);

        return merchantRegisterVO;
    }

    @ApiOperation("上传证件照")
    @PostMapping("/uploadCertification")
    //==================== 上传证件照 ====================
    public String upload(@ApiParam(value = "证件照", required = true) @RequestParam("file") MultipartFile file) throws IOException {
        //原始文件名
        String originalFilename = file.getOriginalFilename();
        //文件后缀名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //保存在七牛云的文件名
        String fileName = UUID.randomUUID() + suffix;
        String fileUrl = fileService.upload(file.getBytes(), fileName);
        //=>r32uoy595.hn-bkt.clouddn.com/4fab5b6d-63d0-4260-bf5f-a496edb2d1a3.png
        return fileUrl;
    }

    @ApiOperation("资质申请")
    @PostMapping("/merchantApplyQualification")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchantInfo", value = "商户认证资料", required = true, dataType = "MerchantDetailVO", paramType = "body")
    })
    //================ 商户资质申请 ================
    public void merchantApplyQualification(@RequestBody MerchantDetailVO merchantDetailVO) {
        //解析token，取出当前登录商户的id
        Long merchantId = SecurityUtil.getMerchantId();
        //vo转dto
        MerchantDTO merchantDTO = BeanUtil.copyProperties(merchantDetailVO, MerchantDTO.class);
        merchantService.merchantApplyQualification(merchantId, merchantDTO);
    }


}









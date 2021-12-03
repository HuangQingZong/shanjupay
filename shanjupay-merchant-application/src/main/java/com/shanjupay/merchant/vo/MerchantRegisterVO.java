package com.shanjupay.merchant.vo;

import com.shanjupay.merchant.valid.MerchantLoginGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 用于接收前端提交的数据
 **/
@ApiModel(value = "MerchantRegisterVO", description = "商户注册信息")
@Data
public class MerchantRegisterVO implements Serializable {

    @ApiModelProperty("手机号")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^\\d{11}$", message = "手机号格式有误")
    private String mobile;

    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空", groups = {MerchantLoginGroup.class})
    private String username;

    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空", groups = {MerchantLoginGroup.class})
    private String password;

    @ApiModelProperty("验证码的key")
    private String verifyKey;

    @ApiModelProperty("验证码")
    private String verifyCode;

}

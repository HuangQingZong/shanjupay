package com.shanjupay.transaction.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("pay_channel")
public class PayChannel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 原始支付渠道名称
     */
    @TableField("channel_name")
    private String channelName;

    /**
     * 原始支付渠道编码
     */
    @TableField("channel_code")
    private String channelCode;


}

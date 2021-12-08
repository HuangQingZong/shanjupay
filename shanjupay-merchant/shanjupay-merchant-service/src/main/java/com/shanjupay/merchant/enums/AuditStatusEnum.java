package com.shanjupay.merchant.enums;

/**
 * 审核状态枚举类
 */
public enum AuditStatusEnum {

    //审核状态：0-未申请, 1-已申请待审核, 2-审核通过, 3-审核拒绝
    notapply("0", "未申请"),
    notAudit("1", "已申请待审核"),
    AuditAgree("2", "审核通过"),
    AuditReject("3", "审核拒绝"),
    ;

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    AuditStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}

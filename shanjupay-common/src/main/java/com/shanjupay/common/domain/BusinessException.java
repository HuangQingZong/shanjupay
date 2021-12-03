package com.shanjupay.common.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义的异常类型
 **/
@Getter
@Setter
public class BusinessException extends RuntimeException {

    /** 错误类 */
    private ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public BusinessException() {
        super();
    }

}

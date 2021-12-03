package com.shanjupay.merchant.common.intercept;

import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.common.domain.ErrorCode;
import com.shanjupay.common.domain.RestErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice //实现对Controller面向切面编程
@Slf4j
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class) //捕获所有异常
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //服务器内部错误
    //==================== 统一处理所有异常 ====================
    public RestErrorResponse processException(Exception e){

        log.error("统一异常处理 ==> ", e);
        RestErrorResponse restErrorResponse = new RestErrorResponse();

        if (e instanceof MethodArgumentNotValidException) {
            //------------ 参数校验异常 ------------
            restErrorResponse.setErrCode(String.valueOf(CommonErrorCode.PARAM_ERROR.getCode()))
                    .setErrMessage(CommonErrorCode.PARAM_ERROR.getDesc());
            BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                restErrorResponse.setErrMessage(fieldError.getDefaultMessage());
            }

        }else if (e instanceof BusinessException) {
            //------------ 自定义异常 ------------
            BusinessException businessException = (BusinessException) e;
            ErrorCode errorCode = businessException.getErrorCode();
            restErrorResponse.setErrCode(String.valueOf(errorCode.getCode()))
                    .setErrMessage(errorCode.getDesc());

        }else if (e instanceof HttpRequestMethodNotSupportedException) {
            //------------ 请求方法不支持 ------------
            restErrorResponse.setErrCode(String.valueOf(CommonErrorCode.UNKNOWN.getCode()))
                    .setErrMessage("请求方法不支持");

        }else{
            //------------ 未知异常 ------------
            restErrorResponse.setErrCode(String.valueOf(CommonErrorCode.UNKNOWN.getCode()))
                    .setErrMessage(CommonErrorCode.UNKNOWN.getDesc());
        }

        return restErrorResponse;
    }

}











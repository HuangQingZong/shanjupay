package com.shanjupay.common.domain;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@ApiModel(value = "RestErrorResponse", description = "错误响应参数包装")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RestErrorResponse {

    private String errCode;
    private String errMessage;

}

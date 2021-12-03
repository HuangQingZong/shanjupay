package com.shanjupay.merchant.service;

import com.shanjupay.common.domain.BusinessException;

public interface FileService {

    /**
     * 上传文件
     *
     * @param byteArr  文件字节数组
     * @param fileName 文件名
     * @return 文件访问路径（绝对的url）
     * @throws BusinessException
     */
    String upload(byte[] byteArr, String fileName) throws BusinessException;
}

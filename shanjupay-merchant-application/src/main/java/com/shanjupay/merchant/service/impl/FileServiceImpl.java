package com.shanjupay.merchant.service.impl;

import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.common.util.QiniuUtils;
import com.shanjupay.merchant.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl implements FileService {

    @Value("${oss.qiniu.url}")
    private String qiniuUrl;

    @Value("${oss.qiniu.accessKey}")
    private String accessKey;

    @Value("${oss.qiniu.secretKey}")
    private String secretKey;

    @Value("${oss.qiniu.bucket}")
    private String bucket;

    @Override
    public String upload(byte[] byteArr, String fileName) throws BusinessException {
        try {
            QiniuUtils.upload(accessKey, secretKey, bucket, byteArr, fileName);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new BusinessException(CommonErrorCode.E_100106);
        }
        //返回文件url
        return qiniuUrl + "/" + fileName;
        //=>r32uoy595.hn-bkt.clouddn.com/4fab5b6d-63d0-4260-bf5f-a496edb2d1a3.png
    }
}

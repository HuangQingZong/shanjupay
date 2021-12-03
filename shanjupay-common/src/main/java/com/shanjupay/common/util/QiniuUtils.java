package com.shanjupay.common.util;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * 七牛云工具类
 */
@Slf4j
public class QiniuUtils {

    /**
     * 文件上传
     */
    public static void upload(String accessKey, String secretKey, String bucket, byte[] bytes, String fileName) throws RuntimeException {
        //创建上传管理器
        Configuration cfg = new Configuration(Region.huanan());
        UploadManager uploadManager = new UploadManager(cfg);
        //认证并获取令牌
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            //上传文件
            Response response = uploadManager.put(bytes, fileName, upToken);
            //解析结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            log.info("七牛云文件上传成功：【{}】", JSON.toJSONString(putRet));
            //=>七牛云文件上传成功：【{"hash":"Fj8_H66H31Y4oSaYdICcd8uN-NcZ","key":"4fab5b6d-63d0-4260-bf5f-a496edb2d1a3.png"}】
        } catch (QiniuException ex) {
            log.error("七牛云文件上传异常：【{}】", ex.response.toString());
            //=>七牛云文件上传异常：【{ResponseInfo:com.qiniu.http.Response@2fd3bc2f,status:631, reqId:acwAAADLOKbsvroW, xlog:X-Log, xvia:, adress:upload-z2.qiniup.com/58.254.139.15:443, duration:0.000000 s, error:no such bucket}】
            String exBody = null;
            try {
                exBody = ex.response.bodyString();
                log.error("七牛云文件上传异常：【{}】", exBody);
                //=>七牛云文件上传异常：【{"error":"no such bucket"}】
            } catch (QiniuException ex2) {
                //ignore
            }
            throw new RuntimeException(exBody);
        }
    }

}

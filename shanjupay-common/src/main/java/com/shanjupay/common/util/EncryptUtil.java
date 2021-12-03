package com.shanjupay.common.util;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;

@Slf4j
public class EncryptUtil {

    /**
     * 将字节数组进行base64编码
     * @param bytes
     * @return
     */
    public static String encodeBase64(byte[] bytes){
        String encoded = Base64.getEncoder().encodeToString(bytes);
        return encoded;
    }

    /**
     * 将字符串进行base64解码
     * @param str
     * @return
     */
    public static byte[]  decodeBase64(String str){
        byte[] bytes = Base64.getDecoder().decode(str);
        return bytes;
    }

    /**
     * 将字符串先进行url编码(+转为%2B，?转为%3F)，防止传输过程中出问题，然后再进行base64编码
     * @param str
     * @return
     */
    public static String encodeUTF8StringBase64(String str){
        String encoded = null;
        try {
            encoded = URLEncoder.encode(str, "utf-8");
            encoded = Base64.getEncoder().encodeToString(encoded.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            log.warn("不支持的编码格式", e);
        }
        return encoded;
    }

    /**
     * 将字符串先进行base64解码，然后再进行url解码
     * @param str
     * @return
     */
    public static String  decodeUTF8StringBase64(String str){
        String decoded = null;
        byte[] bytes = Base64.getDecoder().decode(str);
        try {
            decoded = new String(bytes,"utf-8");
            //=>{"merchantId":1462716677618671617,"mobile":"11122223333","username":"张先生"}
//            decoded = URLDecoder.decode(decoded, "utf-8");
//            //=>{"merchantId":1462716677618671617,"mobile":"11122223333","username":"张先生"}
        }catch(UnsupportedEncodingException e){
            log.warn("不支持的编码格式",e);
        }
        return decoded;
    }


    public static void main(String [] args)  {

    }

}

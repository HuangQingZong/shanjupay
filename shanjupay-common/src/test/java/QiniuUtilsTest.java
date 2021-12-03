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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * 七牛云工具类
 */
public class QiniuUtilsTest {

    //================ 配置信息 ================
    public static final String accessKey = "fS5HOgFJTbtBlUqhSca0j8QxC3ixtlm6-NY2O4jn";
    public static final String secretKey = "vNFfcw5uhA_YvP4AnPCBxf5qG0N_-Z16isEnTmBH";
    public static final String bucket = "shanjupay-hqz211124"; //存储空间名称
    public static final String domainOfBucket = "r32uoy595.hn-bkt.clouddn.com"; //存储空间对应的域名
    public static UploadManager uploadManager =
            new UploadManager(new Configuration(Region.huanan())); //上传管理器
    public static Auth auth = Auth.create(accessKey, secretKey); //认证

    //================ main ================
    public static void main(String[] args) {
        //------------ 文件上传 ------------
//        String filePath = "D:\\41学习_编程\\21、Java教程\\33、项目实战\\21、闪聚支付\\13、闪聚支付_代码\\Java.png";
//        String fileName = testUpload(filePath);
//        //=>00be48bb-7041-4193-ad59-231a7c02349d.png
//        System.out.println(fileName);

        //------------ 文件下载 ------------
        String fileName ="00be48bb-7041-4193-ad59-231a7c02349d.png";
        String downloadUrl = getDownloadUrl(fileName);
        System.out.println(downloadUrl);
        //=>r32uoy595.hn-bkt.clouddn.com/00be48bb-7041-4193-ad59-231a7c02349d.png?e=1637814837&token=fS5HOgFJTbtBlUqhSca0j8QxC3ixtlm6-NY2O4jn:ErPE_leUnqY1aXB2_8SPmNmUwd0
    }

    //================ 获取文件访问链接 ================
    private static String getDownloadUrl(String fileName) {
        String encodedFileName = null;
        try {
            encodedFileName = URLEncoder.encode(fileName, "utf-8").replace("+", "%20");
            //=>00be48bb-7041-4193-ad59-231a7c02349d.png
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //构建公开空间访问链接
        String publicUrl = String.format("%s/%s", domainOfBucket, encodedFileName);
        //=>r32uoy595.hn-bkt.clouddn.com/00be48bb-7041-4193-ad59-231a7c02349d.png
        //私有授权签名
        String downloadUrl = auth.privateDownloadUrl(publicUrl, 3600/*链接过期时间*/);
        //=>r32uoy595.hn-bkt.clouddn.com/00be48bb-7041-4193-ad59-231a7c02349d.png?e=1637814837&token=fS5HOgFJTbtBlUqhSca0j8QxC3ixtlm6-NY2O4jn:ErPE_leUnqY1aXB2_8SPmNmUwd0

        return downloadUrl;
    }

    //================ 上传文件，以字节数组的形式 ================
    private static String testUpload(String filePath) {
        //文件名
        String fileName = UUID.randomUUID() + ".png";
        //上传令牌
        String uploadToken = auth.uploadToken(bucket);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            //得到本地文件的字节数组
            byte[] byteArr = IOUtils.toByteArray(fileInputStream);
            try {
//                Response response = uploadManager.put(filePath/*文件路径*/, fileName, uploadToken);
                Response response = uploadManager.put(byteArr/*字节数组*/, fileName, uploadToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(JSON.toJSONString(putRet));
                //=>{"hash":"Fj8_H66H31Y4oSaYdICcd8uN-NcZ","key":"00be48bb-7041-4193-ad59-231a7c02349d.png"}
                return putRet.key;
            } catch (QiniuException ex) {
                System.err.println(ex.response.toString());
                //=>{ResponseInfo:com.qiniu.http.Response@6f195bc3,status:631, reqId:Y0kAAADoKZQsr7oW, xlog:X-Log, xvia:, adress:upload-z2.qiniup.com/120.83.145.4:443, duration:0.000000 s, error:no such bucket}
                try {
                    System.err.println(ex.response.bodyString());
                    //=>{"error":"no such bucket"}
                } catch (QiniuException ex2) {
                    ex2.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

package com.lee.controller;

import com.lee.util.MessageUtil;
import com.lee.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

@Controller
public class TestController {

    public static String TOKEN = "yanli";

    @RequestMapping("testWeChat")
    public String testWeChat (HttpServletRequest req, HttpServletResponse response) throws Exception {

        System.out.println("-----开始校验签名-----");
        String method = req.getMethod(); // 请求方式 get | post
        if ("get".equalsIgnoreCase(method)) {

            /**
             * 接收微信服务器发送请求时传递过来的参数
             */
            String signature = req.getParameter("signature");
            String timestamp = req.getParameter("timestamp");
            String nonce = req.getParameter("nonce"); //随机数
            String echostr = req.getParameter("echostr");//随机字符串
            if(StringUtil.checkSignature(signature, timestamp, nonce)){
                System.out.println("-----签名校验通过-----");
                //如果校验成功，将得到的随机字符串原路返回
                return echostr;
            }
        } else if ("post".equalsIgnoreCase(method)) {
            String result = "";
            try {
                Map<String,String> map = MessageUtil.parseXml(req);
                result = MessageUtil.buildXml(map);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("发生异常："+ e.getMessage());
            }
            return result;
        }
        return "";

        /**
         * 将token、timestamp、nonce三个参数进行字典序排序
         * 并拼接为一个字符串
         */
//        String sortStr = sort(TOKEN,timestamp,nonce);
//        /**
//         * 字符串进行shal加密
//         */
//        String mySignature = shal(sortStr);
//        /**
//         * 校验微信服务器传递过来的签名 和  加密后的字符串是否一致, 若一致则签名通过
//         */
//        if(!"".equals(signature) && !"".equals(mySignature) && signature.equals(mySignature)){
//            System.out.println("-----签名校验通过-----");
//            return echostr;
//        }else {
//            System.out.println("-----校验签名失败-----");
//            return "";
//        }
    }

    public String sort(String token, String timestamp, String nonce) {
        String[] strArray = {token, timestamp, nonce};
        Arrays.sort(strArray);
        StringBuilder sb = new StringBuilder();
        for (String str : strArray) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 字符串进行shal加密
     * @param str
     * @return
     */
    public String shal(String str){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}

package com.lee.util;



import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageUtil {
    /**
     * 解析微信发来的请求（XML）
     *
     * @param request
     * @return map
     * @throws Exception
     */
    public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap();
        // 从request中取得输入流
        InputStream inputStream = request.getInputStream();
        System.out.println("获取输入流");
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();

        // 遍历所有子节点
        for (Element e : elementList) {
            System.out.println(e.getName() + "|" + e.getText());
            map.put(e.getName(), e.getText());
        }

        // 释放资源
        inputStream.close();
        inputStream = null;
        return map;
    }

    /**
     * 根据消息类型 构造返回消息
     */
    public static String buildXml(Map<String, String> map) {
        String resultContent = "";
        String msgType = map.get("MsgType");
        if (msgType.equals("text")) {
            //文本消息
            resultContent = "文本消息";
        } else if (msgType.equals("image")) {
            //图片消息
            resultContent = "图片消息";
        } else if (msgType.equals("voice")) {
            //语音消息
            resultContent = "语音消息";
        } else if (msgType.equals("video")) {
            //视频消息
            resultContent = "视频消息";
        } else if (msgType.equals("shortvideo")) {
            //小视频消息
            resultContent = "小视频消息";
        } else if (msgType.equals("location")) {
            //地理位置消息
            resultContent = "地理位置消息";
        } else if (msgType.equals("location")) {
            //链接消息
            resultContent = "链接消息";
        } else {
            //不认识的消息
            resultContent = "不认识的消息";
        }
        return buildTextMessage(map, resultContent);
    }

    /**
     * 构造文本消息
     *
     * @param map
     * @param content
     * @return
     */
    private static String buildTextMessage(Map<String, String> map, String content) {
        //发送方帐号
        String fromUserName = map.get("FromUserName");
        // 开发者微信号
        String toUserName = map.get("ToUserName");
        /**
         * 文本消息XML数据格式
         */
        return String.format(
                "<xml>" +
                        "<ToUserName><![CDATA[%s]]></ToUserName>" +
                        "<FromUserName><![CDATA[%s]]></FromUserName>" +
                        "<CreateTime>%s</CreateTime>" +
                        "<MsgType><![CDATA[text]]></MsgType>" +
                        "<Content><![CDATA[%s]]></Content>" + "</xml>",
                fromUserName, toUserName, getUtcTime(), content);
    }

    private static String getUtcTime() {
        Date dt = new Date();// 如果不需要格式,可直接用dt,dt就是当前系统时间
        DateFormat df = new SimpleDateFormat("yyyyMMddhhmm");// 设置显示格式
        String nowTime = df.format(dt);
        long dd = (long) 0;
        try {
            dd = df.parse(nowTime).getTime();
        } catch (Exception e) {

        }
        return String.valueOf(dd);
    }

    public static void main(String[] args) {
        try {
            // 创建SAXReader
            SAXReader reader = new SAXReader();
            Document document = reader.read(new File("D:\\web.xml"));
            // 获取根节点
            Element root = document.getRootElement();
            // 查找指定节点名称QName的所有子节点elements
            List<Element> list = root.elements("emp");
            // 获取emp
            for (Element object : list) {
                System.out.println(object.getName());
                // System.out.println(object.attribute("department").getData());
                for (Element element : (List<Element>) object.elements()) {
                    System.out.print(((Element) element).getName() + ":");
                    System.out.print(element.getText() + " ");
                }
                System.out.println();
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }


}


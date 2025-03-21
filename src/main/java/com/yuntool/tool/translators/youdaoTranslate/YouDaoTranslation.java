package com.yuntool.tool.translators.youdaoTranslate;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


/**
 * 由有道提供，直接调用query方法即可
 * 返回示例：
 * {
 *     "tSpeakUrl": "https://openapi.youdao.com/ttsapi?",
 *     "requestId": "afc14ed2-e6ca-49fe-8f8f-b36e6ff5bbaa",
 *     "query": "Preview on Hover for File Links",
 *     "translation": [
 *         "预览悬停文件链接"
 *     ],
 *     "errorCode": "0",
 *     "dict": {
 *         "url": "yddict://m.youdao.com/dict?le=eng&q=Preview+on+Hover+for+File+Links"
 *     },
 *     "webdict": {
 *         "url": "http://mobile.youdao.com/dict?le=eng&q=Preview+on+Hover+for+File+Links"
 *     },
 *     "l": "en2zh-CHS",
 *     "isWord": false,
 *     "speakUrl": "https://openapi.youdao.com/ttsapi?"
 * }
 */

public class YouDaoTranslation {
    private static Logger logger = LoggerFactory.getLogger(YouDaoTranslation.class);

    private static final String YOUDAO_URL = "https://openapi.youdao.com/api";
    // 去有道查看，这里不能直接用
    private static final String APP_KEY = "2ff2622bfb8b546e";
    //
    private static final String APP_SECRET = "yplb6WDr4yd7sJciHba2cn3ytLrpjvGA";

    public static void main(String[] args) throws IOException {

        Map<String, String> params = new HashMap<String, String>();
        String q = "Decrease body font size";
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("from", "en");
        params.put("to", "zh-CHS");
        params.put("signType", "v3");
        String curtime = String.valueOf(System.currentTimeMillis() / 1000);
        params.put("curtime", curtime);
        String signStr = APP_KEY + truncate(q) + salt + curtime + APP_SECRET;
        String sign = getDigest(signStr);
        params.put("appKey", APP_KEY);
        params.put("q", q);
        params.put("salt", salt);
        params.put("sign", sign);
        params.put("vocabId", "您的用户词表ID");
        /** 处理结果 */
        requestForHttp(YOUDAO_URL, params);
    }

    public static String query(String Str) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        String q = Str;
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("from", "en");
        params.put("to", "zh-CHS");
        params.put("signType", "v3");
        String curtime = String.valueOf(System.currentTimeMillis() / 1000);
        params.put("curtime", curtime);
        String signStr = APP_KEY + truncate(q) + salt + curtime + APP_SECRET;
        String sign = getDigest(signStr);
        params.put("appKey", APP_KEY);
        params.put("q", q);
        params.put("salt", salt);
        params.put("sign", sign);
        params.put("vocabId", "您的用户词表ID");
        /** 处理结果 */
        return requestForHttp(YOUDAO_URL, params);
    }

    public static String requestForHttp(String url, Map<String, String> params) throws IOException {

        String json = null;
        /** 创建HttpClient */
        CloseableHttpClient httpClient = HttpClients.createDefault();

        /** httpPost */
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> en = it.next();
            String key = en.getKey();
            String value = en.getValue();
            paramsList.add(new BasicNameValuePair(key, value));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(paramsList, "UTF-8"));
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        try {
            Header[] contentType = httpResponse.getHeaders("Content-Type");
            logger.info("Content-Type:" + contentType[0].getValue());
            if ("audio/mp3".equals(contentType[0].getValue())) {
                //如果响应是wav
                HttpEntity httpEntity = httpResponse.getEntity();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                httpResponse.getEntity().writeTo(baos);
                byte[] result = baos.toByteArray();
                EntityUtils.consume(httpEntity);
                if (result != null) {//合成成功
                    String file = "合成的音频存储路径" + System.currentTimeMillis() + ".mp3";
                    byte2File(result, file);
                }
            } else {
                /** 响应不是音频流，直接显示结果 */
                HttpEntity httpEntity = httpResponse.getEntity();
                json = EntityUtils.toString(httpEntity, "UTF-8");
                EntityUtils.consume(httpEntity);
                logger.info(json);
                System.out.println(json);
            }
        } finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (IOException e) {
                logger.info("## release resouce error ##" + e);
            }
        }
        return json;
    }


    /**
     * 生成加密字段
     */
    public static String getDigest(String string) {
        if (string == null) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        byte[] btInput = string.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest mdInst = MessageDigest.getInstance("SHA-256");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * @param result 音频字节流
     * @param file   存储路径
     */
    private static void byte2File(byte[] result, String file) {
        File audioFile = new File(file);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(audioFile);
            fos.write(result);

        } catch (Exception e) {
            logger.info(e.toString());
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static String truncate(String q) {
        if (q == null) {
            return null;
        }
        int len = q.length();
        String result;
        return len <= 20 ? q : (q.substring(0, 10) + len + q.substring(len - 10, len));
    }
}

package com.yuntool.tool.translators.baiduTranslate;


import com.alibaba.fastjson.JSON;

public class Main {

    // 在平台申请的APP_ID 详见 http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
    private static final String APP_ID = "20230627001726030";
    private static final String SECURITY_KEY = "R9XbbSizCPFkGEVqgLqF";

    public static void main(String[] args) {
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);

        String query = "hi";
        String transResult = api.getTransResult(query, "auto", "zh");
        System.out.println(transResult);
        String translate = JSON.parseObject(JSON.parseObject(transResult).getJSONArray("trans_result").get(0).toString()).get("dst").toString();
        System.out.println(translate);


    }

}

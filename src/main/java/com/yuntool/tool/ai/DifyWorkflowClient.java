package com.yuntool.tool.ai;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class DifyWorkflowClient {

    private static final String API_KEY = "your-api-key"; // 替换为你的 Dify API Key
    private static final String WORKFLOW_URL = "https://api.dify.ai/v1/workflows/your-workflow-id/run"; // 替换为你的 Workflow ID

    public static void main(String[] args) {
        String message = "你好，Dify！"; // 需要传递的消息
        String response = runWorkflow(message);
        System.out.println("Workflow 响应: " + response);
    }

    /**
     * 发送 message 参数到 Dify Workflow
     */
    public static String runWorkflow(String message) {
        try {
            URL url = new URL(WORKFLOW_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 设置 HTTP 请求头
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // 构造 JSON 请求体
            String jsonPayload = "{ \"inputs\": { \"message\": \"" + message + "\" } }";
            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonPayload.getBytes("UTF-8"));
                os.flush();
            }

            // 读取 API 响应
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                return readResponse(connection);
            } else {
                return "Workflow 调用失败，HTTP 响应码: " + responseCode;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "请求发生异常: " + e.getMessage();
        }
    }

    /**
     * 读取 API 响应内容
     */
    private static String readResponse(HttpURLConnection connection) {
        try (Scanner scanner = new Scanner(connection.getInputStream(), "UTF-8")) {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        } catch (Exception e) {
            e.printStackTrace();
            return "读取响应失败";
        }
    }
}

package com.yuntool.tool.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TongYiUtil {
    // 维护对话历史
    private static final List<Message> chatHistory = new ArrayList<>();

    public static void main(String[] args) {
        String system = "You are a helpful assistant.";

        // 添加系统消息，只添加一次
        chatHistory.add(new Message("system", system));

        // 模拟连续对话
        System.out.println(sendMsg("你是谁？")); // 第一次对话
        System.out.println(sendMsg("你的功能有哪些？")); // 第二次对话
        System.out.println(sendMsg("你可以帮我写代码吗？")); // 第三次对话
    }

    static class Message {
        String role;
        String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

    static class RequestBody {
        String model;
        Message[] messages;

        public RequestBody(String model, Message[] messages) {
            this.model = model;
            this.messages = messages;
        }
    }

    public static String sendMsg(String userMessage) {
        String content = "";
        try {
            // 追加用户的输入到对话历史
            chatHistory.add(new Message("user", userMessage));

            // 创建请求体（包括完整的对话历史）
            RequestBody requestBody = new RequestBody("qwen-plus", chatHistory.toArray(new Message[0]));

            // 将请求体转换为 JSON
            Gson gson = new Gson();
            String jsonInputString = gson.toJson(requestBody);

            // 创建 URL 连接
            URL url = new URL("https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            // 设置请求方法和头信息
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");

            String apiKey = "sk-";
            String auth = "Bearer " + apiKey;
            httpURLConnection.setRequestProperty("Authorization", auth);

            // 启用输入输出流
            httpURLConnection.setDoOutput(true);

            // 发送请求
            try (OutputStream os = httpURLConnection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // 读取响应
            try (BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                // 解析 JSON 响应
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.toString());

                // 提取 'content' 字段
                content = rootNode.path("choices").get(0).path("message").path("content").asText();

                // 追加助手的回复到对话历史
                chatHistory.add(new Message("assistant", content));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return content;
    }
}

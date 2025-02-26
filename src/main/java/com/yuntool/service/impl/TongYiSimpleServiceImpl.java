package com.yuntool.service.impl;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.StreamingChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TongYiSimpleServiceImpl {
    @Autowired
    ChatClient chatClient;

    @Autowired
    StreamingChatClient streamingChatClient;
    @Autowired
    public TongYiSimpleServiceImpl(ChatClient chatClient, StreamingChatClient streamingChatClient) {
        this.chatClient = chatClient;
        this.streamingChatClient = streamingChatClient;
    }

    public String completion(String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return chatClient.call(prompt).getResult().getOutput().getContent();
    }


}

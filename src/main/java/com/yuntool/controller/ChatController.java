package com.yuntool.controller;

import com.yuntool.service.impl.TongYiSimpleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.yuntool.tool.ai.TongYiUtil.sendMsg;

@RestController
@RequestMapping("qwchat")
public class ChatController {
    @Autowired
    TongYiSimpleServiceImpl tongYiSimpleService;


    @GetMapping("/ai")
    public String chat(String message) {
        String completion = tongYiSimpleService.completion(message);
        return completion;
    }

    @GetMapping("/ai2")
    public String chat2(String message) {
        String completion = sendMsg(message);
        return completion;
    }
}

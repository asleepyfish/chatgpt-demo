package com.asleepyfish.controller;

import io.github.asleepyfish.util.OpenAiUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author: asleepyfish
 * @Date: 2023-02-18 14:44
 * @Description: ChatGPTController
 */
@RestController
public class ChatGPTController {
    @PostMapping("/chatTest")
    public List<String> chatTest(String content) {
        return OpenAiUtils.createChatCompletion(content, "testUser");
    }



    @GetMapping("/downloadImage")
    public void downloadImage(String prompt, HttpServletResponse response) {
        OpenAiUtils.downloadImage(prompt, response);
    }
}

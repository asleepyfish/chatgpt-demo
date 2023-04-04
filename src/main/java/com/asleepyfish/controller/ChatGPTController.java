package com.asleepyfish.controller;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import io.github.asleepyfish.util.OpenAiUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
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

    @PostMapping("/streamChatTest")
    public void streamChatTest(String content) {
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(Collections.singletonList(new ChatMessage(ChatMessageRole.USER.value(), content)))
                .n(1)
                .logitBias(new HashMap<>())
                .stream(true)
                .user("user")
                .build();
        OpenAiUtils.createStreamChatCompletion(chatCompletionRequest);
    }
}

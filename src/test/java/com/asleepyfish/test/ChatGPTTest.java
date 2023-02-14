package com.asleepyfish.test;

import io.github.asleepyfish.util.OpenAiUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: asleepyfish
 * @Date: 2023/2/14 10:18
 * @Description: ChatGPTTest
 */
@SpringBootTest
public class ChatGPTTest {
    @Test
    public void testChatGPT() {
        OpenAiUtils.createCompletion("世界上最高的山峰是什么？").forEach(System.out::println);
    }
}

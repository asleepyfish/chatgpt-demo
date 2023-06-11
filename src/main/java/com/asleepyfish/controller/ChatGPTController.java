package com.asleepyfish.controller;

import io.github.asleepyfish.config.ChatGPTProperties;
import io.github.asleepyfish.entity.billing.Billing;
import io.github.asleepyfish.entity.billing.Subscription;
import io.github.asleepyfish.service.OpenAiProxyService;
import io.github.asleepyfish.util.OpenAiUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Author: asleepyfish
 * @Date: 2023-02-18 14:44
 * @Description: ChatGPTController
 */
@RestController
public class ChatGPTController {

    /**
     * 问答
     *
     * @param content 问题
     * @return 答案
     */
    @GetMapping("/chat")
    public List<String> chat(String content) {
        return OpenAiUtils.createChatCompletion(content);
    }

    /**
     * 流式问答，返回到控制台
     */
    @GetMapping("/streamChat")
    public void streamChat(String content) {
        // OpenAiUtils.createStreamChatCompletion(content, System.out);
        // 下面的默认和上面这句代码一样，是输出结果到控制台
        OpenAiUtils.createStreamChatCompletion(content);
    }

    /**
     * 流式问答，输出结果到WEB浏览器端
     */
    @GetMapping("/streamChatWithWeb")
    public void streamChatWithWeb(String content, HttpServletResponse response) throws IOException, InterruptedException {
        // 需要指定response的ContentType为流式输出，且字符编码为UTF-8
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        // 禁用缓存
        response.setHeader("Cache-Control", "no-cache");
        OpenAiUtils.createStreamChatCompletion(content, response.getOutputStream());
    }

    /**
     * 生成图片
     *
     * @param prompt 图片描述
     */
    @GetMapping("/createImage")
    public void createImage(String prompt) {
        System.out.println(OpenAiUtils.createImage(prompt));
    }

    /**
     * 下载图片
     */
    @GetMapping("/downloadImage")
    public void downloadImage(String prompt, HttpServletResponse response) {
        OpenAiUtils.downloadImage(prompt, response);
    }

    @GetMapping("/billing")
    public void billing() {
        String monthUsage = OpenAiUtils.billingUsage("2023-04-01", "2023-05-01");
        System.out.println("四月使用：" + monthUsage + "美元");
        String totalUsage = OpenAiUtils.billingUsage();
        System.out.println("一共使用：" + totalUsage + "美元");
        String stageUsage = OpenAiUtils.billingUsage("2023-01-31");
        System.out.println("自从2023/01/31使用：" + stageUsage + "美元");
        Subscription subscription = OpenAiUtils.subscription();
        System.out.println("订阅信息（包含到期日期，账户总额度等信息）：" + subscription);
        // dueDate为到期日，total为总额度，usage为使用量，balance为余额
        Billing totalBilling = OpenAiUtils.billing();
        System.out.println("历史账单信息：" + totalBilling);
        // 默认不传参的billing方法的使用量usage从2023-01-01开始，如果用户的账单使用早于该日期，可以传入开始日期startDate
        Billing posibleStartBilling = OpenAiUtils.billing("2022-01-01");
        System.out.println("可能的历史账单信息：" + posibleStartBilling);
    }

    /**
     * 自定义Token使用（解决单个SpringBoot项目中只能指定唯一的Token[sk-xxxxxxxxxxxxx]的问题，现在可以自定义ChatGPTProperties内容，添加更多的Token实例）
     */
    @GetMapping("/customToken")
    public void customToken() {
        ChatGPTProperties properties = ChatGPTProperties.builder().token("sk-002xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
                .proxyHost("127.0.0.1")
                .proxyHost("7890")
                .build();
        OpenAiProxyService openAiProxyService = new OpenAiProxyService(properties);
        // 直接使用new出来的openAiProxyService来调用方法，每个OpenAiProxyService都拥有自己的Token。
        // 这样在一个SpringBoot项目中，就可以有多个Token，可以有更多的免费额度供使用了
        openAiProxyService.createStreamChatCompletion("Java的三大特性是什么");
    }

    @GetMapping("/models")
    public void models() {
        System.out.println("models列表：" + OpenAiUtils.listModels());
        System.out.println("=============================================");
        System.out.println("text-davinci-003信息：" + OpenAiUtils.getModel("text-davinci-003"));
    }
}

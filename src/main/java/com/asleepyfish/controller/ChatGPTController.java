package com.asleepyfish.controller;

import com.theokanning.openai.embedding.EmbeddingRequest;
import io.github.asleepyfish.config.ChatGPTProperties;
import io.github.asleepyfish.entity.billing.Billing;
import io.github.asleepyfish.entity.billing.Subscription;
import io.github.asleepyfish.enums.audio.AudioResponseFormatEnum;
import io.github.asleepyfish.enums.edit.EditModelEnum;
import io.github.asleepyfish.enums.embedding.EmbeddingModelEnum;
import io.github.asleepyfish.service.OpenAiProxyService;
import io.github.asleepyfish.util.OpenAiUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: asleepyfish
 * @Date: 2023-02-18 14:44
 * @Description: 注意：所有代码示例均有基于和SpringBoot和直接Main方法调用两种实现。分别在类MainTest和类ChatGPTController中。
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
    @PostMapping("/createImage")
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

    @PostMapping("/billing")
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
    @PostMapping("/customToken")
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

    @PostMapping("/models")
    public void models() {
        System.out.println("models列表：" + OpenAiUtils.listModels());
        System.out.println("=============================================");
        System.out.println("text-davinci-003信息：" + OpenAiUtils.getModel("text-davinci-003"));
    }

    /**
     * 编辑
     */
    @PostMapping("/edit")
    public void edit() {
        String input = "What day of the wek is it?";
        String instruction = "Fix the spelling mistakes";
        System.out.println("编辑前：" + input);
        // 下面这句和OpenAiUtils.edit(input, instruction, EditModelEnum.TEXT_DAVINCI_EDIT_001);是一样的，默认使用模型TEXT_DAVINCI_EDIT_001
        System.out.println("编辑后：" + OpenAiUtils.edit(input, instruction));
        System.out.println("=============================================");
        input = "    public static void mian(String[] args) {\n" +
                "        system.in.println(\"hello world\");\n" +
                "    }";
        instruction = "Fix the code mistakes";
        System.out.println("修正代码前：\n" + input);
        System.out.println("修正代码后：\n" + OpenAiUtils.edit(input, instruction, EditModelEnum.CODE_DAVINCI_EDIT_001));
    }

    @PostMapping("/embeddings")
    public void embeddings() {
        String text = "Once upon a time";
        System.out.println("文本：" + text);
        System.out.println("文本的嵌入向量：" + OpenAiUtils.embeddings(text));
        System.out.println("=============================================");
        String[] texts = {"Once upon a time", "There was a princess"};
        System.out.println("文本数组：" + Arrays.toString(texts));
        EmbeddingRequest embeddingRequest = EmbeddingRequest.builder()
                .model(EmbeddingModelEnum.TEXT_EMBEDDING_ADA_002.getModelName()).input(Arrays.asList(texts)).build();
        System.out.println("文本数组的嵌入向量：" + OpenAiUtils.embeddings(embeddingRequest));
    }

    @PostMapping("/transcription")
    public void transcription() {
        File file = new File("D:/downloads/69906300839135318.mp3");
        System.out.println("语音文件转录后的text文本是：" + OpenAiUtils.transcription(file, AudioResponseFormatEnum.TEXT));
    }

    @PostMapping("/translation")
    public void translation() {
        File file = new File("D:/downloads/69906300839135318.mp3");
        System.out.println("语音文件翻译成英文后的text文本是：" + OpenAiUtils.translation(file, AudioResponseFormatEnum.TEXT));
    }
}
package com.asleepyfish.controller;

import io.github.asleepyfish.config.ChatGPTProperties;
import io.github.asleepyfish.entity.billing.Billing;
import io.github.asleepyfish.entity.billing.Subscription;
import io.github.asleepyfish.enums.edit.EditModelEnum;
import io.github.asleepyfish.service.OpenAiProxyService;
import org.junit.jupiter.api.Test;

/**
 * @Author: asleepyfish
 * @Date: 2023-06-11 21:18
 * @Description: MainTest
 */
public class MainTest {
    @Test
    public void chat() {
        ChatGPTProperties properties = ChatGPTProperties.builder().token("sk-xxx")
                .proxyHost("127.0.0.1")
                .proxyPort(7890)
                .build();
        OpenAiProxyService openAiProxyService = new OpenAiProxyService(properties);
        System.out.println(openAiProxyService.chatCompletion("Go写个程序"));
    }

    @Test
    public void streamChat() {
        ChatGPTProperties properties = ChatGPTProperties.builder().token("sk-xxx")
                .proxyHost("127.0.0.1")
                .proxyPort(7890)
                .build();
        OpenAiProxyService openAiProxyService = new OpenAiProxyService(properties);
        openAiProxyService.createStreamChatCompletion("杭州旅游攻略");
    }

    @Test
    public void createImages() {
        ChatGPTProperties properties = ChatGPTProperties.builder().token("sk-xxx")
                .proxyHost("127.0.0.1")
                .proxyPort(7890)
                .build();
        OpenAiProxyService openAiProxyService = new OpenAiProxyService(properties);
        System.out.println(openAiProxyService.createImages("大白狗"));
    }

    @Test
    public void billing() {
        ChatGPTProperties properties = ChatGPTProperties.builder().token("sk-xxx")
                .proxyHost("127.0.0.1")
                .proxyPort(7890)
                .build();
        OpenAiProxyService openAiProxyService = new OpenAiProxyService(properties);
        String monthUsage = openAiProxyService.billingUsage("2023-04-01", "2023-05-01");
        System.out.println("四月使用：" + monthUsage + "美元");
        String totalUsage = openAiProxyService.billingUsage();
        System.out.println("一共使用：" + totalUsage + "美元");
        String stageUsage = openAiProxyService.billingUsage("2023-01-31");
        System.out.println("自从2023/01/31使用：" + stageUsage + "美元");
        Subscription subscription = openAiProxyService.subscription();
        System.out.println("订阅信息（包含到期日期，账户总额度等信息）：" + subscription);
        // dueDate为到期日，total为总额度，usage为使用量，balance为余额
        Billing totalBilling = openAiProxyService.billing();
        System.out.println("历史账单信息：" + totalBilling);
        // 默认不传参的billing方法的使用量usage从2023-01-01开始，如果用户的账单使用早于该日期，可以传入开始日期startDate
        Billing posibleStartBilling = openAiProxyService.billing("2022-01-01");
        System.out.println("可能的历史账单信息：" + posibleStartBilling);
    }

    @Test
    public void model() {
        ChatGPTProperties properties = ChatGPTProperties.builder().token("sk-xxx")
                .proxyHost("127.0.0.1")
                .proxyPort(7890)
                .build();
        OpenAiProxyService openAiProxyService = new OpenAiProxyService(properties);
        System.out.println("models列表：" + openAiProxyService.listModels());
        System.out.println("=============================================");
        System.out.println("text-davinci-003信息：" + openAiProxyService.getModel("text-davinci-003"));
    }

    @Test
    public void edit() {
        ChatGPTProperties properties = ChatGPTProperties.builder().token("sk-xxx")
                .proxyHost("127.0.0.1")
                .proxyPort(7890)
                .build();
        OpenAiProxyService openAiProxyService = new OpenAiProxyService(properties);
        String input = "What day of the wek is it?";
        String instruction = "Fix the spelling mistakes";
        System.out.println("编辑前：" + input);
        // 下面这句和openAiProxyService.edit(input, instruction, EditModelEnum.TEXT_DAVINCI_EDIT_001);是一样的，默认使用模型TEXT_DAVINCI_EDIT_001
        System.out.println("编辑后：" + openAiProxyService.edit(input, instruction));
        System.out.println("=============================================");
        input = "    public static void mian([String] args) {\n" +
                "        system.in.println(\"hello world\");\n" +
                "    }";
        instruction = "Fix the code mistakes";
        System.out.println("修正代码前：\n" + input);
        System.out.println("修正代码后：\n" + openAiProxyService.edit(input, instruction, EditModelEnum.CODE_DAVINCI_EDIT_001));
    }
}

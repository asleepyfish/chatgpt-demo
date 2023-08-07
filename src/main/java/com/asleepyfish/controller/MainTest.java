package com.asleepyfish.controller;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.finetune.FineTuneRequest;
import com.theokanning.openai.image.CreateImageEditRequest;
import com.theokanning.openai.image.CreateImageVariationRequest;
import com.theokanning.openai.image.ImageResult;
import com.theokanning.openai.moderation.ModerationRequest;
import io.github.asleepyfish.config.ChatGPTProperties;
import io.github.asleepyfish.entity.billing.Billing;
import io.github.asleepyfish.entity.billing.Subscription;
import io.github.asleepyfish.enums.audio.AudioResponseFormatEnum;
import io.github.asleepyfish.enums.edit.EditModelEnum;
import io.github.asleepyfish.enums.embedding.EmbeddingModelEnum;
import io.github.asleepyfish.enums.image.ImageResponseFormatEnum;
import io.github.asleepyfish.enums.image.ImageSizeEnum;
import io.github.asleepyfish.service.OpenAiProxyService;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @Author: asleepyfish
 * @Date: 2023-06-11 21:18
 * @Description: 注意：所有代码示例均有基于和SpringBoot和直接Main方法调用两种实现。分别在类MainTest和类ChatGPTController中。
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

    @Test
    public void embeddings() {
        ChatGPTProperties properties = ChatGPTProperties.builder().token("sk-xxx")
                .proxyHost("127.0.0.1")
                .proxyPort(7890)
                .build();
        OpenAiProxyService openAiProxyService = new OpenAiProxyService(properties);
        // 单文本
        String text = "Once upon a time";
        System.out.println("文本：" + text);
        System.out.println("文本的嵌入向量：" + openAiProxyService.embeddings(text));
        System.out.println("=============================================");
        // 文本数组
        String[] texts = {"Once upon a time", "There was a princess"};
        System.out.println("文本数组：" + Arrays.toString(texts));
        EmbeddingRequest embeddingRequest = EmbeddingRequest.builder()
                .model(EmbeddingModelEnum.TEXT_EMBEDDING_ADA_002.getModelName()).input(Arrays.asList(texts)).build();
        System.out.println("文本数组的嵌入向量：" + openAiProxyService.embeddings(embeddingRequest));
    }

    @Test
    public void transcription() {
        ChatGPTProperties properties = ChatGPTProperties.builder().token("sk-xxx")
                .proxyHost("127.0.0.1")
                .proxyPort(7890)
                .build();
        OpenAiProxyService openAiProxyService = new OpenAiProxyService(properties);
        String filePath = "src/main/resources/audio/想象之中-许嵩.mp3";
        System.out.println("语音文件转录后的json文本是：" + openAiProxyService.transcription(filePath, AudioResponseFormatEnum.JSON));
        // File file = new File("src/main/resources/audio/想象之中-许嵩.mp3");
        // System.out.println("语音文件转录后的json文本是：" + openAiProxyService.transcription(file, AudioResponseFormatEnum.JSON));
    }

    @Test
    public void translation() {
        ChatGPTProperties properties = ChatGPTProperties.builder().token("sk-xxx")
                .proxyHost("127.0.0.1")
                .proxyPort(7890)
                .build();
        OpenAiProxyService openAiProxyService = new OpenAiProxyService(properties);
        String filePath = "src/main/resources/audio/想象之中-许嵩.mp3";
        System.out.println("语音文件翻译成英文后的json文本是：" + openAiProxyService.translation(filePath, AudioResponseFormatEnum.JSON));
        // File file = new File("src/main/resources/audio/想象之中-许嵩.mp3");
        // System.out.println("语音文件翻译成英文后的json文本是：" + openAiProxyService.translation(file, AudioResponseFormatEnum.JSON));
    }

    @Test
    public void createImageEdit() {
        ChatGPTProperties properties = ChatGPTProperties.builder().token("sk-xxx")
                .proxyHost("127.0.0.1")
                .proxyPort(7890)
                .build();
        OpenAiProxyService openAiProxyService = new OpenAiProxyService(properties);
        CreateImageEditRequest createImageEditRequest = CreateImageEditRequest.builder().prompt("A sunlit indoor lounge area with a pool containing a flamingo")
                .n(1).size(ImageSizeEnum.S512x512.getSize()).responseFormat(ImageResponseFormatEnum.URL.getResponseFormat()).build();
        ImageResult imageEdit = openAiProxyService.createImageEdit(createImageEditRequest, "src/main/resources/image/img.png", "src/main/resources/image/mask.png");
        System.out.println("图片编辑结果：" + imageEdit);
    }

    @Test
    public void createImageVariation() {
        ChatGPTProperties properties = ChatGPTProperties.builder().token("sk-xxx")
                .proxyHost("127.0.0.1")
                .proxyPort(7890)
                .build();
        OpenAiProxyService openAiProxyService = new OpenAiProxyService(properties);
        CreateImageVariationRequest createImageVariationRequest = CreateImageVariationRequest.builder()
                .n(2).size(ImageSizeEnum.S512x512.getSize()).responseFormat(ImageResponseFormatEnum.URL.getResponseFormat()).build();
        ImageResult imageVariation = openAiProxyService.createImageVariation(createImageVariationRequest, "src/main/resources/image/img.png");
        System.out.println("图片变体结果：" + imageVariation);
    }

    /**
     * 文件操作（下面文件操作入参，用户可根据实际情况自行补全）
     */
    @Test
    public void files() {
        ChatGPTProperties properties = ChatGPTProperties.builder().token("sk-xxx")
                .proxyHost("127.0.0.1")
                .proxyPort(7890)
                .build();
        OpenAiProxyService openAiProxyService = new OpenAiProxyService(properties);
        // 上传文件
        System.out.println("上传文件信息：" + openAiProxyService.uploadFile("", ""));
        // 获取文件列表
        System.out.println("文件列表：" + openAiProxyService.listFiles());
        // 获取文件信息
        System.out.println("文件信息：" + openAiProxyService.retrieveFile(""));
        // 获取文件内容
        System.out.println("文件内容：" + openAiProxyService.retrieveFileContent(""));
        // 删除文件
        System.out.println("删除文件信息：" + openAiProxyService.deleteFile(""));
    }

    @Test
    public void fileTune() {
        ChatGPTProperties properties = ChatGPTProperties.builder().token("sk-xxx")
                .proxyHost("127.0.0.1")
                .proxyPort(7890)
                .build();
        OpenAiProxyService openAiProxyService = new OpenAiProxyService(properties);
        // 创建微调
        FineTuneRequest fineTuneRequest = FineTuneRequest.builder().trainingFile("").build();
        System.out.println("创建微调信息：" + openAiProxyService.createFineTune(fineTuneRequest));
        // 创建微调完成
        CompletionRequest completionRequest = CompletionRequest.builder().build();
        System.out.println("创建微调完成信息：" + openAiProxyService.createFineTuneCompletion(completionRequest));
        // 获取微调列表
        System.out.println("获取微调列表：" + openAiProxyService.listFineTunes());
        // 获取微调信息
        System.out.println("获取微调信息：" + openAiProxyService.retrieveFineTune(""));
        // 取消微调
        System.out.println("取消微调信息：" + openAiProxyService.cancelFineTune(""));
        // 列出微调事件
        System.out.println("列出微调事件：" + openAiProxyService.listFineTuneEvents(""));
        // 删除微调
        System.out.println("删除微调信息：" + openAiProxyService.deleteFineTune(""));
    }

    @Test
    public void moderation() {
        ChatGPTProperties properties = ChatGPTProperties.builder().token("sk-xxx")
                .proxyHost("127.0.0.1")
                .proxyPort(7890)
                .build();
        OpenAiProxyService openAiProxyService = new OpenAiProxyService(properties);
        // 创建moderation
        ModerationRequest moderationRequest = ModerationRequest.builder().input("I want to kill them.").build();
        System.out.println("创建moderation信息：" + openAiProxyService.createModeration(moderationRequest));
    }

    @Test
    public void baseUrl() {
        ChatGPTProperties properties = ChatGPTProperties.builder().token("sk-xxx")
                // 自定义baseUrl
                .baseUrl("https://openai.api2d.net/")
                .build();
        OpenAiProxyService openAiProxyService = new OpenAiProxyService(properties);
        System.out.println("models列表：" + openAiProxyService.listModels());
    }

/*    @Test
    public void okHttpClient() {
        ChatGPTProperties properties = ChatGPTProperties.builder().token("sk-xxx")
                .proxyHost("127.0.0.1")
                .proxyPort(7890)
                .build();
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(100);
        dispatcher.setMaxRequestsPerHost(10);
        // 自定义okHttpClient
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new AuthenticationInterceptor(properties.getToken()))
                .connectionPool(new ConnectionPool(100, 10, TimeUnit.SECONDS))
                .readTimeout(Duration.ZERO.toMillis(), TimeUnit.MILLISECONDS)
                .connectTimeout(Duration.ZERO.toMillis(), TimeUnit.MILLISECONDS)
                .hostnameVerifier((hostname, session) -> true)
                .proxy(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(properties.getProxyHost(), properties.getProxyPort())))
                .proxyAuthenticator((route, response) -> {
                    String credential = Credentials.basic("proxyUsername", "proxyPassword");
                    return response.request().newBuilder()
                            .header("Proxy-Authorization", credential)
                            .build();
                })
                .dispatcher(dispatcher)
                .build();
        // 下面的openAiProxyService使用自定义的okHttpClient
        OpenAiProxyService openAiProxyService = new OpenAiProxyService(properties, okHttpClient);
        System.out.println("models列表：" + openAiProxyService.listModels());
    }*/
}

package com.asleepyfish.controller;

/**
 * @Author: asleepyfish
 * @Date: 2023/7/17 14:38
 * @Description: TODO
 */

import okhttp3.*;

import java.io.IOException;

public class OkHttpExample {

    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n    \"model\": \"text-embedding-ada-002\",\n    \"input\": \"Hello\"\n}");
        Request request = new Request.Builder()
                .url("https://apps.ichati.cn/1d6f32f8-b59d-46f8-85e9-7d434b83acd7/v1/embeddings")
                .method("POST", body)
                .addHeader("Authorization", "Bearer xxxxx")
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }
}


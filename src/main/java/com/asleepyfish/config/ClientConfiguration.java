package com.asleepyfish.config;

import io.github.asleepyfish.config.ChatGPTProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;


/**
 * @Author: asleepyfish
 * @Date: 2023-08-06 21:52
 * @Description: client configuration
 */
@Configuration
public class ClientConfiguration {

    @Autowired
    private ChatGPTProperties properties;

/*    @Bean
    public OkHttpClient okHttpClient() {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(100);
        dispatcher.setMaxRequestsPerHost(10);
        return new OkHttpClient.Builder()
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
    }*/
}

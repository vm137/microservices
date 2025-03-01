package com.epam.resource.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpConfig {

    @Bean
    OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }
}

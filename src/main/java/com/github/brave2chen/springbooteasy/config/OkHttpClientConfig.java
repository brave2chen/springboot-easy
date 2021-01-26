package com.github.brave2chen.springbooteasy.config;

import io.opentracing.Tracer;
import io.opentracing.contrib.okhttp3.TracingCallFactory;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @author brave2chen
 */
@Configuration
@Slf4j
public class OkHttpClientConfig {
    @Value("${okHttpClient.call-timeout: 3000}")
    private Long timeout;

    @Autowired
    private Tracer tracer;

    @Bean
    protected Call.Factory okHttpClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        Call.Factory client = new TracingCallFactory(httpClient, tracer);
        return client;
    }

    @Bean
    protected Call.Factory okHttpClientWithTimeout() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().callTimeout(Duration.ofMillis(timeout)).build();
        Call.Factory client = new TracingCallFactory(okHttpClient, tracer);
        return client;
    }
}

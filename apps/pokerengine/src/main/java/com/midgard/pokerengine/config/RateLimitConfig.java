package com.midgard.pokerengine.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "rate-limit")
@EnableConfigurationProperties
public class RateLimitConfig {
    private int capacity;
    private int timeWindow;
    private boolean enabled;

    @Bean
    public Bucket bucket() {
        return Bucket.builder()
                    .addLimit(Bandwidth.classic(capacity, Refill.intervally(capacity, Duration.ofSeconds(timeWindow))))
                    .build();
    }
}
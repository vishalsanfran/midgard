package com.midgard.pokerengine.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "rate-limit")
@Getter
@Setter
public class RateLimitConfig {
    private int capacity = 100;
    private int timeWindow = 60;
    private boolean enabled = true;

    public Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.classic(capacity, 
            Refill.intervally(capacity, Duration.ofSeconds(timeWindow)));
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}
package com.midgard.pokerengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.midgard.pokerengine.config.RateLimitConfig;

@SpringBootApplication
@EnableConfigurationProperties(RateLimitConfig.class)
public class PokerengineApplication {

    public static void main(String[] args) {
        SpringApplication.run(PokerengineApplication.class, args);
    }
}

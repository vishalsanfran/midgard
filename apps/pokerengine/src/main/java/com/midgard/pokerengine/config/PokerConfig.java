package com.midgard.pokerengine.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "poker")
public class PokerConfig {
  private List<Integer> validHandSizes;
}
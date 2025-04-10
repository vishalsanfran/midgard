package com.midgard.pokerengine.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI pokerEngineOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Poker Engine API")
                        .description("REST API for Poker Game Engine")
                        .version("1.0"));
    }
}
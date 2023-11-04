package com.medialistmaker.list.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfiguration {

    @Bean
    CustomFeignDecoder feignDecoder() {
        return new CustomFeignDecoder();
    }
}

package com.medialistmaker.list.configuration.feign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfiguration {

    @Bean
    CustomFeignDecoder feignDecoder() {
        return new CustomFeignDecoder();
    }
}

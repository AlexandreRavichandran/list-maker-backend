package com.medialistmaker.movie.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfiguration {

    @Bean
    CustomFeignDecoder feignDecoder() {
        return new CustomFeignDecoder();
    }

}

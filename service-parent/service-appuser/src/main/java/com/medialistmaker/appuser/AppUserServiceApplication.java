package com.medialistmaker.appuser;

import com.medialistmaker.appuser.domain.AppUser;
import com.medialistmaker.appuser.repository.AppUserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableFeignClients
public class AppUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppUserServiceApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    //@Bean
    CommandLineRunner run(AppUserRepository appUserRepository) {
        return args -> {
            AppUser appUser = AppUser
                    .builder()
                    .username("test")
                    .password(this.passwordEncoder().encode("test"))
                    .build();

            appUserRepository.save(appUser);
        };
    }
}

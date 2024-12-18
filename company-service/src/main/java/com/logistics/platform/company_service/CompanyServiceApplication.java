package com.logistics.platform.company_service;

import com.logistics.platform.company_service.presentation.global.ex.FeignErrorDecoder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
public class CompanyServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(CompanyServiceApplication.class, args);
  }

//  @Bean
//  public FeignErrorDecoder getFeignErrorDecoder() {
//    return new FeignErrorDecoder();
//  }

}

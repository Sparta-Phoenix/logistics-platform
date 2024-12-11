package com.logistics.platform.company_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CompanyServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(CompanyServiceApplication.class, args);
  }

}

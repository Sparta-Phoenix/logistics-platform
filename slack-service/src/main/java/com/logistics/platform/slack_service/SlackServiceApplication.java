package com.logistics.platform.slack_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SlackServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlackServiceApplication.class, args);
	}

}

package com.logistics.platform.product_service.infrastructure.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "product API",
        version = "v1",
        description = "product API 입니다"
    )
)
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        // Security Scheme (Bearer Token)
        SecurityScheme apiKey = new SecurityScheme()
            .type(SecurityScheme.Type.APIKEY)
            .in(SecurityScheme.In.HEADER)
            .name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement()
            .addList("Bearer Token");

        // OpenAPI 설정 (CORS와 API 보안)
        return new OpenAPI()
            .components(new Components().addSecuritySchemes("Bearer Token", apiKey))
            .addSecurityItem(securityRequirement)
            .addServersItem(new Server().url("/")); // CORS 및 Swagger 서버 경로 설정
    }

}

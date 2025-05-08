package com.bernerus.boxy.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI boxyOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Boxy API")
                        .description("API for determining optimal box sizes for packaging items")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Andreas Bernérus")
                                .url("https://github.com/abernerus")
                                .email("contact@bernerusit.se"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("/")
                                .description("Default Server URL")
                ));
    }
}

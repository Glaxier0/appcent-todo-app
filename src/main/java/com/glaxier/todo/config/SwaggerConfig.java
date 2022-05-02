package com.glaxier.todo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class SwaggerConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("To-Do App REST API")
                        .description("Appcent To-Do App REST API by Tahir Çelik")
                        .version("1.0")
                        .license(new License().name("MIT License")
                                .url("https://github.com/Glaxier0/appcent-todo-app/blob/main/LICENSE")));
    }
}

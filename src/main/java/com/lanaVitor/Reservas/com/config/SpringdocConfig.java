package com.lanaVitor.Reservas.com.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringdocConfig {

    @Value("${springdoc.openapi.path-info.basePath}")
    private String url;

    @Value("${springdoc.openapi.path-info.basePath2}")
    private String urlGitHub;

    @Bean
    public OpenAPI springDocOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("API Restful Resort.com")
                        .description("Documentação da API Resort.com utilizando springdoc")
                        .version("1.1.0")
                        .license(new License().name("Apache 2.0").url(url)))
                        .externalDocs(new ExternalDocumentation()
                        .description("Link do Repositório da Aplicação - API Restful Documentation")
                        .url(urlGitHub));
    }
}

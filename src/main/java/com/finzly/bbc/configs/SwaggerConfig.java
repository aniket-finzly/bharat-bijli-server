package com.finzly.bbc.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class SwaggerConfig implements WebMvcConfigurer {

    @Bean
    public OpenAPI customOpenAPI () {
        return new OpenAPI ()
                .info (createApiInfo ())
                .addSecurityItem (createSecurityRequirement ())
                .components (createComponents ())
                .tags (List.of ()) // Add tags here if needed
                .servers (createServers ());
    }

    private Info createApiInfo () {
        return new Info ()
                .title ("Bharat Bijli App API")
                .version ("1.0")
                .description ("""
                        API reference for developers
                        
                        ### Company Overview
                        Bharat Bijli Corporation (BBC) is a private electric power utility company based in Pune. \
                        As a distributor of electricity, BBC serves over 10,000 families in the local township. \
                        With a dedicated back office operations team of 20 employees, BBC efficiently manages electricity bill payments and customer service.
                        
                        ### IT Portals
                        BBC is developing two IT portals:
                        1. **BBC - Utility Bill Pay (BBC-UBP)**: A customer-facing portal for bill payments, usage tracking, and bill downloads.
                        2. **BBC Ops**: A back office portal for operations employees to manage customer data and generate invoices.
                        
                        ### Contact
                        Email: aniket.finzly@outlook.com
                        GitHub: https://github.com/aniket-finzly
                        
                        Email: ganesh.finzly@outlook.com
                        GitHub: https://github.com/ganesh-finzly
                        """)
                .contact (createContact ());
    }

    private Contact createContact () {
        return new Contact ()
                .name ("Aniket Tiwari")
                .url ("https://github.com/aniket-finzly")
                .email ("aniket.finzly@outlook.com");
    }

    private SecurityRequirement createSecurityRequirement () {
        return new SecurityRequirement ().addList ("bearerAuth");
    }

    private Components createComponents () {
        return new Components ()
                .addSecuritySchemes ("bearerAuth", createSecurityScheme ());
    }

    private SecurityScheme createSecurityScheme () {
        return new SecurityScheme ()
                .type (SecurityScheme.Type.HTTP)
                .scheme ("bearer")
                .bearerFormat ("JWT");
    }

    private List<io.swagger.v3.oas.models.servers.Server> createServers () {
        return List.of (
                new io.swagger.v3.oas.models.servers.Server ()
                        .url ("http://localhost:8080")
                        .description ("Local server"),
                new io.swagger.v3.oas.models.servers.Server ()
                        .url ("https://bharat-bijli-app.herokuapp.com")
                        .description ("Production server")
        );
    }

    @Override
    public void addResourceHandlers (ResourceHandlerRegistry registry) {
        registry.addResourceHandler ("swagger-ui.html")
                .addResourceLocations ("classpath:/META-INF/resources/");
        registry.addResourceHandler ("/v3/api-docs/**")
                .addResourceLocations ("classpath:/META-INF/resources/v3/api-docs/");
    }
}


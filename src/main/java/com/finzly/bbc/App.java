package com.finzly.bbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.finzly.bbc.models")
@EnableJpaRepositories("com.finzly.bbc.repositories")
@EnableCaching
public class App {

    public static void main (String[] args) {
        SpringApplication.run (App.class, args);
    }
}

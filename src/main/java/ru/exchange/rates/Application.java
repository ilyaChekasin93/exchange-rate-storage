package ru.exchange.rates;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableAsync
@SpringBootApplication
@EnableJpaRepositories(basePackages = "ru.exchange.rates.dao")
public class Application {
    public static void main(String[] args) { SpringApplication.run(Application.class, args); }
}
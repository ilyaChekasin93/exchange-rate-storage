package ru.exchange.rates.client.impl;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import ru.exchange.rates.client.ExchangeRatesClient;
import ru.exchange.rates.client.handler.CustomErrorHandler;


public abstract class AbstractExchangeRaresClient implements ExchangeRatesClient {

    protected RestTemplate restTemplate;

    protected String password;

    public AbstractExchangeRaresClient(RestTemplateBuilder restTemplateBuilder, CustomErrorHandler customErrorHandler) {
        this.restTemplate = restTemplateBuilder.build();
        restTemplate.setErrorHandler(customErrorHandler);
        String passwordEnvName = getPasswordEnvName();
        this.password = System.getenv(passwordEnvName);
    }

    protected abstract String getPasswordEnvName();

}

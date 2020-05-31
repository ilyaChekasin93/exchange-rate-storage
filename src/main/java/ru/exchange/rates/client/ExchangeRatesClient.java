package ru.exchange.rates.client;

import ru.exchange.rates.dto.ExchangeRatesDto;

import java.util.concurrent.CompletableFuture;


public interface ExchangeRatesClient {

    CompletableFuture<ExchangeRatesDto> getAllRates();

}

package ru.exchange.rates.client.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.exchange.rates.client.ExchangeRatesClient;
import ru.exchange.rates.client.handler.ExchangeRatesClientErrorHandler;
import ru.exchange.rates.dto.ExchangeRatesDto;
import ru.exchange.rates.mapper.FixerIoMapper;
import ru.exchange.rates.model.FixerIoResponse;


import java.util.concurrent.CompletableFuture;

import static ru.exchange.rates.utils.DateUtils.getCurrentDate;


@Component
public class FixerIoClient implements ExchangeRatesClient {

    private RestTemplate restTemplate;

    private FixerIoMapper mapper;

    @Value("${FIXER_PASSWORD}")
    private String password;

    private static final String BASE_URL = "http://data.fixer.io/api/";


    public FixerIoClient(RestTemplateBuilder restTemplateBuilder, ExchangeRatesClientErrorHandler customErrorHandler, FixerIoMapper mapper) {
        this.restTemplate = restTemplateBuilder.build();
        restTemplate.setErrorHandler(customErrorHandler);
        this.mapper = mapper;
    }

    @Async
    public CompletableFuture<ExchangeRatesDto> getAllRates(){
        String url = UriComponentsBuilder
                .fromHttpUrl(BASE_URL)
                .queryParam("access_key", password)
                .path("latest")
                .toUriString();

        ResponseEntity<FixerIoResponse> responseEntity = restTemplate.getForEntity(url, FixerIoResponse.class);
        FixerIoResponse fixerIoResponse = responseEntity.getBody();

        ExchangeRatesDto listExchangeRateDto = mapper.fixerIoResponse2ExchangeRatesDto(fixerIoResponse);

        String currentDate = getCurrentDate();
        listExchangeRateDto.setDate(currentDate);

        listExchangeRateDto.setSource(BASE_URL);

        return CompletableFuture.completedFuture(listExchangeRateDto);
    }

}

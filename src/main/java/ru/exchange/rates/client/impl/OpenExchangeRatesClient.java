package ru.exchange.rates.client.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import ru.exchange.rates.client.ExchangeRatesClient;
import ru.exchange.rates.dto.ExchangeRatesDto;
import ru.exchange.rates.client.handler.ExchangeRatesClientErrorHandler;
import ru.exchange.rates.mapper.OpenExchangeRatesMapper;
import ru.exchange.rates.model.OpenExchangeRatesResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.CompletableFuture;

import static ru.exchange.rates.utils.DateUtils.getCurrentDate;


@Component
public class OpenExchangeRatesClient implements ExchangeRatesClient {

    private RestTemplate restTemplate;

    private OpenExchangeRatesMapper mapper;

    @Value("${OER_PASSWORD}")
    private String password;

    private static final String BASE_URL = "https://openexchangerates.org/api/";


    public OpenExchangeRatesClient(RestTemplateBuilder restTemplateBuilder, ExchangeRatesClientErrorHandler customErrorHandler, OpenExchangeRatesMapper mapper) {
        this.restTemplate = restTemplateBuilder.build();
        restTemplate.setErrorHandler(customErrorHandler);
        this.mapper = mapper;
    }

    public CompletableFuture<ExchangeRatesDto> getAllRates(){
        String url = UriComponentsBuilder
                .fromHttpUrl(BASE_URL)
                .queryParam("app_id", password)
                .path("latest.json")
                .toUriString();

        ResponseEntity<OpenExchangeRatesResponse> responseEntity = restTemplate.getForEntity(url, OpenExchangeRatesResponse.class);
        OpenExchangeRatesResponse openExchangeRatesResponse = responseEntity.getBody();

        ExchangeRatesDto listExchangeRateDto = mapper.openExchangeRatesResponse2ExchangeRatesDto(openExchangeRatesResponse);

        String currentDate = getCurrentDate();
        listExchangeRateDto.setDate(currentDate);

        listExchangeRateDto.setSource(BASE_URL);

        return CompletableFuture.completedFuture(listExchangeRateDto);
    }
}

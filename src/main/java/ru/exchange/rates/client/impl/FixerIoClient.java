package ru.exchange.rates.client.impl;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import ru.exchange.rates.client.handler.CustomErrorHandler;
import ru.exchange.rates.dto.ExchangeRateDto;
import ru.exchange.rates.dto.ExchangeRatesDto;
import ru.exchange.rates.mapper.Mapper;
import ru.exchange.rates.model.FixerIoResponse;
import ru.exchange.rates.utils.Helpers;

import java.util.List;


@Component
public class FixerIoClient extends AbstractExchangeRaresClient {

    private Mapper mapper;

    private static final String PASSWORD_ENV_NAME = "FIXER_PASSWORD";

    private static final String BASE_URL = "http://data.fixer.io/api/";


    public FixerIoClient(RestTemplateBuilder restTemplateBuilder, CustomErrorHandler customErrorHandler, Mapper mapper) {
        super(restTemplateBuilder, customErrorHandler);
        this.mapper = mapper;
    }

    public ExchangeRatesDto getAllRates(){
        String url = UriComponentsBuilder
                .fromHttpUrl(BASE_URL)
                .queryParam("access_key", password)
                .path("latest")
                .toUriString();

        ResponseEntity<FixerIoResponse> responseEntity = restTemplate.getForEntity(url, FixerIoResponse.class);
        FixerIoResponse openExchangeRatesResponse = responseEntity.getBody();
        List<ExchangeRateDto> exchangeRatesDtos = mapper.fixerIoResponse2ExchangeRatesDto(openExchangeRatesResponse);
        String currentDate = Helpers.getCurrentDate();

        ExchangeRatesDto listExchangeRateDto = new ExchangeRatesDto();
        listExchangeRateDto.setRates(exchangeRatesDtos);
        listExchangeRateDto.setDate(currentDate);
        listExchangeRateDto.setSource(BASE_URL);

        return listExchangeRateDto;
    }

    protected String getPasswordEnvName() {
        return PASSWORD_ENV_NAME;
    }
}

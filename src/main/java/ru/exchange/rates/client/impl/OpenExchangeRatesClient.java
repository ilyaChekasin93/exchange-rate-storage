package ru.exchange.rates.client.impl;

import ru.exchange.rates.dto.ExchangeRateDto;
import ru.exchange.rates.dto.ExchangeRatesDto;
import ru.exchange.rates.client.handler.CustomErrorHandler;
import ru.exchange.rates.mapper.Mapper;
import ru.exchange.rates.model.OpenExchangeRatesResponse;
import ru.exchange.rates.utils.Helpers;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


@Component
public class OpenExchangeRatesClient extends AbstractExchangeRaresClient {

    private Mapper mapper;

    private static final String PASSWORD_ENV_NAME = "OER_PASSWORD";

    private static final String BASE_URL = "https://openexchangerates.org/api/";


    public OpenExchangeRatesClient(RestTemplateBuilder restTemplateBuilder, CustomErrorHandler customErrorHandler, Mapper mapper) {
        super(restTemplateBuilder, customErrorHandler);
        this.mapper = mapper;
    }

    public ExchangeRatesDto getAllRates(){
        String url = UriComponentsBuilder
                .fromHttpUrl(BASE_URL)
                .queryParam("app_id", password)
                .path("latest.json")
                .toUriString();

        ResponseEntity<OpenExchangeRatesResponse> responseEntity = restTemplate.getForEntity(url, OpenExchangeRatesResponse.class);
        OpenExchangeRatesResponse openExchangeRatesResponse = responseEntity.getBody();
        List<ExchangeRateDto> exchangeRatesDtos = mapper.openExchangeRatesResponse2ExchangeRatesDto(openExchangeRatesResponse);
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

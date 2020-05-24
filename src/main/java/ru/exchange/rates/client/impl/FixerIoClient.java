package ru.exchange.rates.client.impl;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.exchange.rates.client.ExchangeRatesClient;
import ru.exchange.rates.client.handler.CustomErrorHandler;
import ru.exchange.rates.dto.ExchangeRateDto;
import ru.exchange.rates.dto.ExchangeRatesDto;
import ru.exchange.rates.mapper.ExchangeRateMapper;
import ru.exchange.rates.model.FixerIoResponse;

import java.util.List;
import java.util.stream.Collectors;

import static ru.exchange.rates.utils.DateUtils.getCurrentDate;


@Component
public class FixerIoClient implements ExchangeRatesClient {

    private ExchangeRateMapper mapper;

    private RestTemplate restTemplate;

    private String password;

    private static final String PASSWORD_ENV_NAME = "FIXER_PASSWORD";

    private static final String BASE_URL = "http://data.fixer.io/api/";


    public FixerIoClient(RestTemplateBuilder restTemplateBuilder, CustomErrorHandler customErrorHandler, ExchangeRateMapper mapper) {
        this.restTemplate = restTemplateBuilder.build();
        restTemplate.setErrorHandler(customErrorHandler);
        this.password = System.getenv(PASSWORD_ENV_NAME);
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

        return fixerIoResponse2ExchangeRatesDto(openExchangeRatesResponse);
    }

    private ExchangeRatesDto fixerIoResponse2ExchangeRatesDto(FixerIoResponse fixerIoResponse) {
        List<ExchangeRateDto> exchangeRatesDtos = fixerIoResponse2ExchangeRateDtoList(fixerIoResponse);

        ExchangeRatesDto listExchangeRateDto = new ExchangeRatesDto();
        listExchangeRateDto.setRates(exchangeRatesDtos);

        String currentDate = getCurrentDate();
        listExchangeRateDto.setDate(currentDate);

        listExchangeRateDto.setSource(BASE_URL);

        return listExchangeRateDto;
    }


    private List<ExchangeRateDto> fixerIoResponse2ExchangeRateDtoList(FixerIoResponse fixerIoResponse) {
        return fixerIoResponse
                .getRates()
                .entrySet()
                .stream()
                .map(exchangeRate -> {
                    ExchangeRateDto exchangeRatesDto = mapper.exchangeRate2ExchangeRateDto(exchangeRate);

                    String currencyFrom = fixerIoResponse.getBase();
                    exchangeRatesDto.setCurrencyFrom(currencyFrom);

                    return exchangeRatesDto;
                }).collect(Collectors.toList());
    }
}

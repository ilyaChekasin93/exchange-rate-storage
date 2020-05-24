package ru.exchange.rates.client.impl;

import org.springframework.web.client.RestTemplate;
import ru.exchange.rates.client.ExchangeRatesClient;
import ru.exchange.rates.dto.ExchangeRateDto;
import ru.exchange.rates.dto.ExchangeRatesDto;
import ru.exchange.rates.client.handler.CustomErrorHandler;
import ru.exchange.rates.mapper.ExchangeRateMapper;
import ru.exchange.rates.model.OpenExchangeRatesResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

import static ru.exchange.rates.utils.DateUtils.getCurrentDate;


@Component
public class OpenExchangeRatesClient implements ExchangeRatesClient {

    private ExchangeRateMapper mapper;

    private RestTemplate restTemplate;

    private String password;

    private static final String PASSWORD_ENV_NAME = "OER_PASSWORD";

    private static final String BASE_URL = "https://openexchangerates.org/api/";


    public OpenExchangeRatesClient(RestTemplateBuilder restTemplateBuilder, CustomErrorHandler customErrorHandler, ExchangeRateMapper mapper) {
        this.restTemplate = restTemplateBuilder.build();
        restTemplate.setErrorHandler(customErrorHandler);
        this.password = System.getenv(PASSWORD_ENV_NAME);
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

        return openExchangeRatesResponse2ExchangeRatesDto(openExchangeRatesResponse);
    }

    private ExchangeRatesDto openExchangeRatesResponse2ExchangeRatesDto(OpenExchangeRatesResponse openExchangeRatesResponse){
        List<ExchangeRateDto> exchangeRatesDtos = openExchangeRatesResponse2ExchangeRateDtoList(openExchangeRatesResponse);

        ExchangeRatesDto listExchangeRateDto = new ExchangeRatesDto();
        listExchangeRateDto.setRates(exchangeRatesDtos);

        String currentDate = getCurrentDate();
        listExchangeRateDto.setDate(currentDate);

        listExchangeRateDto.setSource(BASE_URL);

        return listExchangeRateDto;
    }

    private List<ExchangeRateDto> openExchangeRatesResponse2ExchangeRateDtoList(OpenExchangeRatesResponse openExchangeRatesResponse) {
        return openExchangeRatesResponse
                .getRates()
                .entrySet()
                .stream()
                .map(exchangeRate -> {
                    ExchangeRateDto exchangeRatesDto = mapper.exchangeRate2ExchangeRateDto(exchangeRate);

                    String currencyFrom = openExchangeRatesResponse.getBase();
                    exchangeRatesDto.setCurrencyFrom(currencyFrom);

                    return exchangeRatesDto;
                }).collect(Collectors.toList());
    }
}

package ru.exchange.rates.mapper.impl;

import org.springframework.stereotype.Component;
import ru.exchange.rates.dto.ExchangeRateDto;
import ru.exchange.rates.dto.ExchangeRatesDto;
import ru.exchange.rates.mapper.ExchangeRateMapper;
import ru.exchange.rates.mapper.OpenExchangeRatesMapper;
import ru.exchange.rates.model.OpenExchangeRatesResponse;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class OpenExchangeRatesMapperImpl implements OpenExchangeRatesMapper {

    private ExchangeRateMapper exchangeRateMapper;

    public OpenExchangeRatesMapperImpl(ExchangeRateMapper exchangeRateMapper){
        this.exchangeRateMapper = exchangeRateMapper;
    }

    public ExchangeRatesDto openExchangeRatesResponse2ExchangeRatesDto(OpenExchangeRatesResponse openExchangeRatesResponse){
        List<ExchangeRateDto> exchangeRatesDtos = openExchangeRatesResponse2ExchangeRateDtoList(openExchangeRatesResponse);

        ExchangeRatesDto listExchangeRateDto = new ExchangeRatesDto();
        listExchangeRateDto.setRates(exchangeRatesDtos);

        return listExchangeRateDto;
    }

    private List<ExchangeRateDto> openExchangeRatesResponse2ExchangeRateDtoList(OpenExchangeRatesResponse openExchangeRatesResponse) {
        return openExchangeRatesResponse
                .getRates()
                .entrySet()
                .stream()
                .map(exchangeRate -> {
                    ExchangeRateDto exchangeRatesDto = exchangeRateMapper.exchangeRate2ExchangeRateDto(exchangeRate);

                    String currencyFrom = openExchangeRatesResponse.getBase();
                    exchangeRatesDto.setCurrencyFrom(currencyFrom);

                    return exchangeRatesDto;
                }).collect(Collectors.toList());
    }

}

package ru.exchange.rates.mapper.impl;

import org.springframework.stereotype.Component;
import ru.exchange.rates.dto.ExchangeRateDto;
import ru.exchange.rates.dto.ExchangeRatesDto;
import ru.exchange.rates.mapper.ExchangeRateMapper;
import ru.exchange.rates.mapper.FixerIoMapper;
import ru.exchange.rates.model.FixerIoResponse;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class FixerIoMapperImpl implements FixerIoMapper {

    private ExchangeRateMapper exchangeRateMapper;

    public FixerIoMapperImpl(ExchangeRateMapper exchangeRateMapper){
        this.exchangeRateMapper = exchangeRateMapper;
    }

    public ExchangeRatesDto fixerIoResponse2ExchangeRatesDto(FixerIoResponse fixerIoResponse) {
        List<ExchangeRateDto> exchangeRatesDtos = fixerIoResponse2ExchangeRateDtoList(fixerIoResponse);

        ExchangeRatesDto listExchangeRateDto = new ExchangeRatesDto();
        listExchangeRateDto.setRates(exchangeRatesDtos);

        return listExchangeRateDto;
    }


    private List<ExchangeRateDto> fixerIoResponse2ExchangeRateDtoList(FixerIoResponse fixerIoResponse) {
        return fixerIoResponse
                .getRates()
                .entrySet()
                .stream()
                .map(exchangeRate -> {
                    ExchangeRateDto exchangeRatesDto = exchangeRateMapper.exchangeRate2ExchangeRateDto(exchangeRate);

                    String currencyFrom = fixerIoResponse.getBase();
                    exchangeRatesDto.setCurrencyFrom(currencyFrom);

                    return exchangeRatesDto;
                }).collect(Collectors.toList());
    }

}

package ru.exchange.rates.mapper;

import org.springframework.stereotype.Component;
import ru.exchange.rates.dto.ExchangeRateDto;
import ru.exchange.rates.model.ExchangeRateResponse;
import ru.exchange.rates.model.OpenExchangeRatesResponse;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Mapper {


    public List<ExchangeRateDto> openExchangeRatesResponse2ExchangeRatesDto(OpenExchangeRatesResponse openExchangeRatesResponse){
        List<ExchangeRateDto> exchangeRatesDtoList = openExchangeRatesResponse.getRates().entrySet().stream().map(e -> {
            String name = e.getKey();
            Double value = e.getValue();
            ExchangeRateDto exchangeRatesDto = new ExchangeRateDto();
            String base = openExchangeRatesResponse.getBase();
            exchangeRatesDto.setBase(base);
            exchangeRatesDto.setRateName(name);
            exchangeRatesDto.setRateValue(value);
            return exchangeRatesDto;
        }).collect(Collectors.toList());

        return exchangeRatesDtoList;
    }

    public ExchangeRateResponse exchangeRateDto2ExchangeRateResponse(ExchangeRateDto exchangeRateDto){
        ExchangeRateResponse exchangeRateResponse = new ExchangeRateResponse();

        exchangeRateResponse.setBase(exchangeRateDto.getBase());
        exchangeRateResponse.setRateName(exchangeRateDto.getRateName());
        exchangeRateResponse.setRateValue(exchangeRateDto.getRateValue());

        return exchangeRateResponse;
    }

}

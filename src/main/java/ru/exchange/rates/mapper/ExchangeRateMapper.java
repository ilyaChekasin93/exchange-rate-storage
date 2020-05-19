package ru.exchange.rates.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.exchange.rates.dto.ExchangeRateDto;
import ru.exchange.rates.model.ExchangeRateResponse;

import java.util.Map;


@Mapper(componentModel = "spring")
public interface ExchangeRateMapper {

    @Mapping(source = "currencyFrom", target = "from")
    @Mapping(source = "currencyTo", target = "to")
    @Mapping(source = "exchangeRateValue", target = "value")
    ExchangeRateResponse exchangeRateDto2ExchangeRateResponse(ExchangeRateDto exchangeRateDto);

    @Mapping(expression = "java(exchangeRate.getKey())", target = "currencyTo")
    @Mapping(expression = "java(exchangeRate.getValue())", target = "exchangeRateValue")
    ExchangeRateDto exchangeRate2ExchangeRateDto(Map.Entry<String, Double> exchangeRate);

}

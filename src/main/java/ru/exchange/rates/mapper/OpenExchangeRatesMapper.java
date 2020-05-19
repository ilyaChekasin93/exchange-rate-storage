package ru.exchange.rates.mapper;

import ru.exchange.rates.dto.ExchangeRatesDto;
import ru.exchange.rates.model.OpenExchangeRatesResponse;

public interface OpenExchangeRatesMapper {

    ExchangeRatesDto openExchangeRatesResponse2ExchangeRatesDto(OpenExchangeRatesResponse openExchangeRatesResponse);

}

package ru.exchange.rates.mapper;

import ru.exchange.rates.dto.ExchangeRatesDto;
import ru.exchange.rates.model.FixerIoResponse;

public interface FixerIoMapper {

    ExchangeRatesDto fixerIoResponse2ExchangeRatesDto(FixerIoResponse fixerIoResponse);

}

package ru.exchange.rates.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExchangeRatesDto {

    private String source;

    private String date;

    private List<ExchangeRateDto> rates;

}

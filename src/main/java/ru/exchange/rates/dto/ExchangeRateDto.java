package ru.exchange.rates.dto;

import lombok.Data;


@Data
public class ExchangeRateDto {

    private String base;

    private String rateName;

    private Double rateValue;

}

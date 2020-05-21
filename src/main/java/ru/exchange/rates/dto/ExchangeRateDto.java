package ru.exchange.rates.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateDto {

    private String base;

    private String rateName;

    private Double rateValue;

}

package ru.exchange.rates.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AllCurrencyByFromResponse {

    private String time;

    private String message;

    private List<String> availableСurrencies;

}

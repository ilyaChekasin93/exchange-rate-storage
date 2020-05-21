package ru.exchange.rates.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AllCurrencyByFromResponse {

    String message;

    List<String> availableСurrencies;

}

package ru.exchange.rates.model;

import lombok.Data;

import java.util.HashMap;


@Data
public class OpenExchangeRatesResponse {

    private String disclaimer;

    private String license;

    private Integer timestamp;

    private String base;

    private HashMap<String, Double> rates;

}

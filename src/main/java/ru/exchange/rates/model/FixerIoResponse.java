package ru.exchange.rates.model;

import lombok.Data;

import java.util.Map;


@Data
public class FixerIoResponse {

    private String success;

    private String date;

    private Integer timestamp;

    private String base;

    private Map<String, Double> rates;

}

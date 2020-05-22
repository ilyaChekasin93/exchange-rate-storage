package ru.exchange.rates.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExchangeRateNotFoundException extends RuntimeException {

    private String currencyTo;

    private String currencyFrom;

    public ExchangeRateNotFoundException(String currencyTo, String currencyFrom){
        super(String.format("Exchange rate from %s to %s not found", currencyFrom, currencyTo));
        this.currencyTo = currencyTo;
        this.currencyFrom = currencyFrom;
    }

}

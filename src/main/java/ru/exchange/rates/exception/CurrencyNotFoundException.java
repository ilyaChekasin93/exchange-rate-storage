package ru.exchange.rates.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CurrencyNotFoundException extends RuntimeException {

    private String currencyName;

    private String message;

    public CurrencyNotFoundException(String currencyName){
        this.currencyName = currencyName;
        this.message = String.format("Currency %s not found", currencyName);
    }

}

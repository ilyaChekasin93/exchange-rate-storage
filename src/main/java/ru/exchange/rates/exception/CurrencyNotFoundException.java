package ru.exchange.rates.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CurrencyNotFoundException extends RuntimeException {

    private String currency;

    public CurrencyNotFoundException(String currency){
        super(String.format("Currency %s not found", currency));
        this.currency = currency;
    }

}

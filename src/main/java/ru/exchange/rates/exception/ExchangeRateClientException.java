package ru.exchange.rates.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
public class ExchangeRateClientException extends AppBaseException {

    private static final String ERROR_PREFIX = "There was an error with the currency exchange client: ";

    public ExchangeRateClientException(String message){
        super(ERROR_PREFIX + message);
    }

}

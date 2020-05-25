package ru.exchange.rates.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.exchange.rates.exception.CurrencyNotFoundException;
import ru.exchange.rates.exception.ExchangeRateClientException;
import ru.exchange.rates.exception.ExchangeRateNotFoundException;
import ru.exchange.rates.model.AllCurrencyByFromResponse;
import ru.exchange.rates.model.ErrorResponse;
import ru.exchange.rates.service.ExchangeRatesService;

import java.util.List;


@RestController
@ControllerAdvice
public class ExchangeRatesExceptionHandler extends ResponseEntityExceptionHandler {

    private ExchangeRatesService exchangeRatesService;

    public ExchangeRatesExceptionHandler(ExchangeRatesService exchangeRatesService){
        this.exchangeRatesService = exchangeRatesService;
    }

    @ExceptionHandler(ExchangeRateNotFoundException.class)
    public final AllCurrencyByFromResponse handleExchangeRateNotFoundException(ExchangeRateNotFoundException exception) {
        String time = exception.getTime();
        String message = exception.getMessage();
        String currencyTo = exception.getCurrencyTo();
        List<String> allToNames = exchangeRatesService.getAllCurrencyByFrom(currencyTo);

        return new AllCurrencyByFromResponse(time, message, allToNames);
    }

    @ExceptionHandler(CurrencyNotFoundException.class)
    public final ErrorResponse handleCurrencyNotFoundException(CurrencyNotFoundException exception) {
        String time = exception.getTime();
        String message = exception.getMessage();

        return new ErrorResponse(time, message);
    }

    @ExceptionHandler(ExchangeRateClientException.class)
    public final ErrorResponse handleExchangeRateClientException(ExchangeRateClientException exception) {
        String time = exception.getTime();
        String message = exception.getMessage();

        return new ErrorResponse(time, message);
    }

}

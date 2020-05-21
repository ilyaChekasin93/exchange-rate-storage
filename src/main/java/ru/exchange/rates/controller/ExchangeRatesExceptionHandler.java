package ru.exchange.rates.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.exchange.rates.exception.CurrencyNotFoundException;
import ru.exchange.rates.model.AllCurrencyByFromResponse;
import ru.exchange.rates.service.ExchangeRatesService;

import java.util.List;


@RestController
@ControllerAdvice
public class ExchangeRatesExceptionHandler extends ResponseEntityExceptionHandler {

    private ExchangeRatesService exchangeRatesService;

    public ExchangeRatesExceptionHandler(ExchangeRatesService exchangeRatesService){
        this.exchangeRatesService = exchangeRatesService;
    }

    @ExceptionHandler(CurrencyNotFoundException.class)
    public final AllCurrencyByFromResponse handleCurrencyToNotFoundException(CurrencyNotFoundException exception) {
        String message = exception.getMessage();
        String notFoundCurrency = exception.getCurrencyName();
        List<String> allToNames = exchangeRatesService.getAllCurrencyByFrom(notFoundCurrency);

        return new AllCurrencyByFromResponse(message, allToNames);
    }

}

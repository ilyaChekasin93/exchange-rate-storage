package ru.exchange.rates.controller;

import org.springframework.web.bind.annotation.PathVariable;
import ru.exchange.rates.service.ExchangeRatesService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping()
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExchangeRatesController {

    private ExchangeRatesService ratesService;

    public ExchangeRatesController(ExchangeRatesService ratesService){
        this.ratesService = ratesService;
    }

    @GetMapping(path = "/rates/{from}/{to}")
    public String getRate(@PathVariable("from") String from, @PathVariable("to") String to) {
        ratesService.getLastExchangeRate(from, to);

        return "Hallow";
    }

}

package ru.exchange.rates.controller;

import org.springframework.web.bind.annotation.PathVariable;
import ru.exchange.rates.dto.ExchangeRateDto;
import ru.exchange.rates.mapper.Mapper;
import ru.exchange.rates.model.ExchangeRateResponse;
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

    private Mapper mapper;

    public ExchangeRatesController(ExchangeRatesService ratesService, Mapper mapper){
        this.ratesService = ratesService;
        this.mapper = mapper;
    }

    @GetMapping(path = "/rates/{from}/{to}")
    public ExchangeRateResponse getRateByFromAndTo(@PathVariable("from") String from, @PathVariable("to") String to) {
        ExchangeRateDto exchangeRateDto = ratesService.getLastExchangeRate(from, to);

        return mapper.exchangeRateDto2ExchangeRateResponse(exchangeRateDto);
    }

}

package ru.exchange.rates.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.exchange.rates.dao.CurrencyEntityRepo;
import ru.exchange.rates.dao.ExchangeRateRepo;
import ru.exchange.rates.dao.ExchangeRateSourceRepo;
import ru.exchange.rates.exception.CurrencyNotFoundException;
import ru.exchange.rates.exception.ExchangeRateNotFoundException;
import ru.exchange.rates.factory.ExchangeRateClientFactory;
import org.springframework.stereotype.Service;
import ru.exchange.rates.dao.entity.CurrencyEntity;
import ru.exchange.rates.client.ExchangeRatesClient;
import ru.exchange.rates.dao.entity.ExchangeRateEntity;
import ru.exchange.rates.dao.entity.ExchangeRateSourceEntity;
import ru.exchange.rates.dto.ExchangeRateDto;
import ru.exchange.rates.dto.ExchangeRatesDto;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ExchangeRatesService {

    private List<ExchangeRatesClient> clients;

    private ExchangeRateSourceRepo exchangeRateSourceRepo;

    private CurrencyEntityRepo currencyEntityRepo;

    private ExchangeRateRepo exchangeRateRepo;


    public ExchangeRatesService(ExchangeRateClientFactory clientFactory, ExchangeRateSourceRepo exchangeRateSourceRepo,
                                CurrencyEntityRepo сurrencyEntityRepo, ExchangeRateRepo exchangeRateRepo) {
        this.clients = clientFactory.getAllExchangeRatesClients();
        this.exchangeRateSourceRepo = exchangeRateSourceRepo;
        this.currencyEntityRepo = сurrencyEntityRepo;
        this.exchangeRateRepo = exchangeRateRepo;
    }

    @Transactional
    public void saveRatesFromAllSources() {
        List<ExchangeRatesDto> exchangeRatesDtolist = clients.stream()
                .map(ExchangeRatesClient::getAllRates)
                .collect(Collectors.toList());

        List<ExchangeRateEntity> exchangeRateEntities = exchangeRatesDtolist.stream()
                .map(ratesDto -> exchangeRatesDto2ListExchangeRateEntity(ratesDto))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        exchangeRateRepo.saveAll(exchangeRateEntities);
    }

    public ExchangeRateDto getLastExchangeRate(String from, String to) {
        CurrencyEntity currencyFrom = currencyEntityRepo.findByName(from)
                .orElseThrow(() -> new CurrencyNotFoundException(from));

        CurrencyEntity currencyTo = currencyEntityRepo.findByName(to)
                .orElseThrow(() -> new CurrencyNotFoundException(to));

        Pageable pageable = PageRequest.of(0, 1);

        Double exchangeRateValue = exchangeRateRepo.findByFromAndTo(currencyFrom, currencyTo, pageable)
                .get()
                .findFirst()
                .orElseThrow(() -> new ExchangeRateNotFoundException(from, to))
                .getValue();

        return new ExchangeRateDto(from, to, exchangeRateValue);
    }

    public List<String> getAllCurrencyByFrom(String from) {
        CurrencyEntity currencyFrom = currencyEntityRepo.findByName(from)
                .orElseThrow(() -> new CurrencyNotFoundException(from));

        List<ExchangeRateEntity> exchangeRateEntities = exchangeRateRepo.findByFrom(currencyFrom);

        return exchangeRateEntities.stream()
                .map(exchangeRateEntity -> exchangeRateEntity.getCurrencyTo().getName())
                .collect(Collectors.toList());
    }

    private List<ExchangeRateEntity> exchangeRatesDto2ListExchangeRateEntity(ExchangeRatesDto ratesDto){
        String time = ratesDto.getDate();

        String sourceUrl = ratesDto.getSource();
        ExchangeRateSourceEntity exchangeRateSourceEntity = getSourceByUrl(sourceUrl);

        List<ExchangeRateDto> exchangeRateDtoList = ratesDto.getRates();

        List<ExchangeRateEntity> exchangeRateEntities = exchangeRateDtoList.stream().map(rateDto -> {
            ExchangeRateEntity exchangeRateEntity = exchangeRateDto2ExchangeRateEntity(rateDto);

            exchangeRateEntity.setSource(exchangeRateSourceEntity);
            exchangeRateSourceEntity.addExchangeRates(exchangeRateEntity);

            exchangeRateEntity.setTime(time);

            return exchangeRateEntity;

        }).collect(Collectors.toList());

        return exchangeRateEntities;
    }

    private ExchangeRateEntity exchangeRateDto2ExchangeRateEntity(ExchangeRateDto rateDto) {
        String currencyTo = rateDto.getCurrencyTo();
        CurrencyEntity currencyEntity = getCurrencyEntityByName(currencyTo);

        String currencyFrom = rateDto.getCurrencyFrom();
        CurrencyEntity baseCurrencyEntity = getCurrencyEntityByName(currencyFrom);

        Double exchangeRateValue = rateDto.getExchangeRateValue();

        ExchangeRateEntity exchangeRateEntity = new ExchangeRateEntity();

        exchangeRateEntity.setCurrencyTo(currencyEntity);
        exchangeRateEntity.setCurrencyFrom(baseCurrencyEntity);

        currencyEntity.addExchangeRatesFrom(exchangeRateEntity);
        baseCurrencyEntity.addExchangeRatesFrom(exchangeRateEntity);

        currencyEntity.addExchangeRatesTo(exchangeRateEntity);
        baseCurrencyEntity.addExchangeRatesTo(exchangeRateEntity);

        exchangeRateEntity.setValue(exchangeRateValue);

        return exchangeRateEntity;
    }

    private ExchangeRateSourceEntity getSourceByUrl(String url) {
        return exchangeRateSourceRepo.findByUrl(url)
                .orElseGet(() -> exchangeRateSourceRepo.saveAndFlush(new ExchangeRateSourceEntity(url)));
    }

    private CurrencyEntity getCurrencyEntityByName(String currencyName) {
        return currencyEntityRepo.findByName(currencyName)
                .orElseGet(() -> currencyEntityRepo.saveAndFlush(new CurrencyEntity(currencyName)));
    }
}

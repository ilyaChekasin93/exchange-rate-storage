package ru.exchange.rates.service;

import org.springframework.transaction.annotation.Transactional;
import ru.exchange.rates.dao.CurrencyEntityRepo;
import ru.exchange.rates.dao.ExchangeRateRepo;
import ru.exchange.rates.dao.ExchangeRateSourceRepo;
import ru.exchange.rates.factory.ExchangeRateClientFactory;
import org.springframework.stereotype.Service;
import ru.exchange.rates.dao.entity.CurrencyEntity;
import ru.exchange.rates.client.ExchangeRatesClient;
import ru.exchange.rates.dao.entity.ExchangeRateEntity;
import ru.exchange.rates.dao.entity.ExchangeRateSourceEntity;
import ru.exchange.rates.dto.ExchangeRateDto;
import ru.exchange.rates.dto.ExchangeRatesDto;

import java.util.List;
import java.util.Optional;
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

    public void getLastExchangeRate(String from, String to) {
        CurrencyEntity currencyFrom = currencyEntityRepo.findByName(from).get();
        CurrencyEntity currencyTo = currencyEntityRepo.findByName(to).get();

        exchangeRateRepo.findByFromAndTo(currencyFrom, currencyTo);
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
        String currencyName = rateDto.getRateName();
        CurrencyEntity currencyEntity = getCurrencyEntityByName(currencyName);

        String baseCurrencyName = rateDto.getBase();
        CurrencyEntity baseCurrencyEntity = getCurrencyEntityByName(baseCurrencyName);

        Double rateValue = rateDto.getRateValue();

        ExchangeRateEntity exchangeRateEntity = new ExchangeRateEntity();

        exchangeRateEntity.setCurrency(currencyEntity);
        exchangeRateEntity.setBase(baseCurrencyEntity);

        currencyEntity.addExchangeRatesFrom(exchangeRateEntity);
        baseCurrencyEntity.addExchangeRatesFrom(exchangeRateEntity);

        currencyEntity.addExchangeRatesTo(exchangeRateEntity);
        baseCurrencyEntity.addExchangeRatesTo(exchangeRateEntity);

        exchangeRateEntity.setValue(rateValue);

        return exchangeRateEntity;
    }

    private ExchangeRateSourceEntity getSourceByUrl(String url) {
        Optional<ExchangeRateSourceEntity> optionalSourceEntity = exchangeRateSourceRepo.findByUrl(url);

        return optionalSourceEntity.isPresent()
                ? optionalSourceEntity.get()
                : exchangeRateSourceRepo.save(new ExchangeRateSourceEntity(url));
    }

    private CurrencyEntity getCurrencyEntityByName(String currencyName) {
        Optional<CurrencyEntity> optionalCurrencyEntity = currencyEntityRepo.findByName(currencyName);

        return optionalCurrencyEntity.isPresent()
                ? optionalCurrencyEntity.get()
                : currencyEntityRepo.saveAndFlush(new CurrencyEntity(currencyName));
    }
}

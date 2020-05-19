package ru.exchange.rates.service;

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


    public ExchangeRatesService(ExchangeRateClientFactory clientFactory, ExchangeRateSourceRepo exchangeRateSourceRepo, CurrencyEntityRepo сurrencyEntityRepo, ExchangeRateRepo exchangeRateRepo) {
        this.clients = clientFactory.getAllExchangeRatesClients();
        this.exchangeRateSourceRepo = exchangeRateSourceRepo;
        this.currencyEntityRepo = сurrencyEntityRepo;
        this.exchangeRateRepo = exchangeRateRepo;
    }

    public void saveRatesFromAllSources(){
        List<ExchangeRatesDto> exchangeRatesDtolist = clients.stream().map(c -> c.getAllRates()).collect(Collectors.toList());


        List<ExchangeRateEntity> result = exchangeRatesDtolist.stream().map(exchangeRatesDto -> {

            String time = exchangeRatesDto.getDate();

            String sourceUrl = exchangeRatesDto.getSource();
            ExchangeRateSourceEntity exchangeRateSourceEntity
                    = exchangeRateSourceRepo.findByUrl(sourceUrl).orElse(new ExchangeRateSourceEntity(sourceUrl));

            List<ExchangeRateDto> exchangeRateDtoList = exchangeRatesDto.getRates();

            List<ExchangeRateEntity> exchangeRateEntities = exchangeRateDtoList.stream().map(exchangeRateDto -> {

                String currencyName = exchangeRateDto.getRateName();
                CurrencyEntity currencyEntity = getCurrencyEntityByName(currencyName);

                String baseCurrencyName = exchangeRateDto.getBase();
                CurrencyEntity baseCurrencyEntity = getCurrencyEntityByName(baseCurrencyName);

                Double rateValue = exchangeRateDto.getRateValue();

                ExchangeRateEntity exchangeRateEntity = new ExchangeRateEntity();

                exchangeRateEntity.setCurrency(currencyEntity);
                exchangeRateEntity.setBase(baseCurrencyEntity);

                exchangeRateEntity.setTime(time);
                exchangeRateEntity.setSource(exchangeRateSourceEntity);
                exchangeRateEntity.setValue(rateValue);

                return exchangeRateEntity;

            }).collect(Collectors.toList());

            return exchangeRateEntities;

        }).flatMap(List::stream).collect(Collectors.toList());

        exchangeRateRepo.saveAll(result);
    }

    public void getLastExchangeRate(String from, String to){
        CurrencyEntity currencyFrom = currencyEntityRepo.findByName(from).get();
        CurrencyEntity currencyTo = currencyEntityRepo.findByName(to).get();

        exchangeRateRepo.findByFromAndTo(currencyFrom, currencyTo);
    }

    private CurrencyEntity getCurrencyEntityByName(String currencyName){
        Optional<CurrencyEntity> optionalCurrencyEntity = currencyEntityRepo.findByName(currencyName);
        CurrencyEntity result;

        if(optionalCurrencyEntity.isPresent()){
            result = optionalCurrencyEntity.get();
        }else {
            CurrencyEntity currencyEntity = new CurrencyEntity(currencyName);
            currencyEntityRepo.save(currencyEntity);
            result = currencyEntity;
        }

        return result;
    }
}

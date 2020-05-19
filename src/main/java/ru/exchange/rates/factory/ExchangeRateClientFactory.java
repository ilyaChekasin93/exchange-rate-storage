package ru.exchange.rates.factory;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.stereotype.Component;
import ru.exchange.rates.client.ExchangeRatesClient;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class ExchangeRateClientFactory {

    private ListableBeanFactory beanFactory;


    public ExchangeRateClientFactory(ListableBeanFactory beanFactory){
        this.beanFactory = beanFactory;
    }

    public List<ExchangeRatesClient> getAllExchangeRatesClients(){
        Collection<ExchangeRatesClient> expressionStrategyCollection = beanFactory.getBeansOfType(ExchangeRatesClient.class).values();
        return expressionStrategyCollection.stream().collect(Collectors.toList());
    }

}

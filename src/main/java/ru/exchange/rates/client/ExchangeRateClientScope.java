package ru.exchange.rates.client;

import org.springframework.stereotype.Component;
import ru.exchange.rates.dto.ExchangeRatesDto;
import ru.exchange.rates.exception.ExchangeRateClientException;
import ru.exchange.rates.factory.ExchangeRateClientFactory;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


@Component
public class ExchangeRateClientScope {

    private List<ExchangeRatesClient> clients;


    public ExchangeRateClientScope(ExchangeRateClientFactory clientFactory){
        this.clients = clientFactory.getAllExchangeRatesClients();
    }

    public List<ExchangeRatesDto> getAllRates(){
        return clients.stream()
                .map(client -> {
                    ExchangeRatesDto exchangeRatesDto;

                    try {
                        exchangeRatesDto = client.getAllRates().get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new ExchangeRateClientException(e.getMessage());
                    }

                    if(exchangeRatesDto == null)
                        throw new ExchangeRateClientException(
                                String.format("The client did not return the result"));

                    return exchangeRatesDto;

                }).collect(Collectors.toList());
    }

}

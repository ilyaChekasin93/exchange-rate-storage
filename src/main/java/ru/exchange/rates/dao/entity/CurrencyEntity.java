package ru.exchange.rates.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyEntity  {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "base", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ExchangeRateEntity> exchangeRatesTo;

    @OneToMany(mappedBy = "currency", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ExchangeRateEntity> exchangeRatesFrom;

    public CurrencyEntity(String name){
        this.name = name;
    }

    public void addExchangeRatesTo(ExchangeRateEntity exchangeRateEntity){
        exchangeRatesTo = exchangeRatesTo == null ? new ArrayList<>() : exchangeRatesTo;
        exchangeRatesTo.add(exchangeRateEntity);
    }

    public void addExchangeRatesFrom(ExchangeRateEntity exchangeRateEntity){
        exchangeRatesFrom = exchangeRatesFrom == null ? new ArrayList<>() : exchangeRatesFrom;
        exchangeRatesFrom.add(exchangeRateEntity);
    }
}

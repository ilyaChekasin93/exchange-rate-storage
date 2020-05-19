package ru.exchange.rates.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "EXCHANGE_RATE")
public class ExchangeRateEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private ExchangeRateSourceEntity source;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private CurrencyEntity currencyFrom;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private CurrencyEntity currencyTo;

    private Double value;

    private String time;


}
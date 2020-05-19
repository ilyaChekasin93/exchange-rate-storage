package ru.exchange.rates.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateSourceEntity {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "source", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ExchangeRateEntity> exchangeRates;

    private String url;

    public ExchangeRateSourceEntity(String url){
        this.url = url;
    }

}

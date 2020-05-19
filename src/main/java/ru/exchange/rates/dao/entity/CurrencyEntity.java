package ru.exchange.rates.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyEntity  {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public CurrencyEntity(String name){
        this.name = name;
    }

}

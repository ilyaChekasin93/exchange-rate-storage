package ru.exchange.rates.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.exchange.rates.dao.entity.CurrencyEntity;
import ru.exchange.rates.dao.entity.ExchangeRateEntity;

import java.util.List;
import java.util.Optional;


@Repository
public interface ExchangeRateRepo extends JpaRepository<ExchangeRateEntity, Long> {

    List<ExchangeRateEntity> findAll();

    @Query("SELECT e FROM ExchangeRateEntity e where e.currencyFrom = :from AND e.currencyTo = :to")
    Optional<ExchangeRateEntity> findByFromAndTo(@Param("from") CurrencyEntity from, @Param("to") CurrencyEntity to);

    @Query("SELECT e FROM ExchangeRateEntity e where e.currencyFrom = :from")
    List<ExchangeRateEntity> findByFrom(@Param("from") CurrencyEntity from);

}
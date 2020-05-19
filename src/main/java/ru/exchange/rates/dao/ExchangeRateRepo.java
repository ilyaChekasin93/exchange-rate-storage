package ru.exchange.rates.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.exchange.rates.dao.entity.CurrencyEntity;
import ru.exchange.rates.dao.entity.ExchangeRateEntity;

import java.util.List;


@Repository
public interface ExchangeRateRepo extends JpaRepository<ExchangeRateEntity, Long> {

    List<ExchangeRateEntity> findAll();

    @Query("SELECT e FROM ExchangeRateEntity e where e.currencyFrom = :from AND e.currencyTo = :to ORDER BY e.id DESC")
    Page<ExchangeRateEntity> findByFromAndTo(@Param("from") CurrencyEntity from, @Param("to") CurrencyEntity to, Pageable pageable);

    @Query("SELECT e FROM ExchangeRateEntity e where e.currencyFrom = :from")
    List<ExchangeRateEntity> findByFrom(@Param("from") CurrencyEntity from);

}
package ru.exchange.rates.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.exchange.rates.dao.entity.CurrencyEntity;
import ru.exchange.rates.dao.entity.ExchangeRateEntity;

import java.util.List;
import java.util.Optional;


@Repository
public interface ExchangeRateRepo extends CrudRepository<ExchangeRateEntity, Long> {

    List<ExchangeRateEntity> findAll();

    @Query("SELECT e FROM ExchangeRateEntity e where e.base = :from AND e.currency = :to")
    Optional<ExchangeRateEntity> findByFromAndTo(@Param("from") CurrencyEntity from, @Param("to") CurrencyEntity to);

}
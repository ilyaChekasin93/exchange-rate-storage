package ru.exchange.rates.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.exchange.rates.dao.entity.ExchangeRateSourceEntity;

import java.util.List;
import java.util.Optional;


@Repository
public interface ExchangeRateSourceRepo extends CrudRepository<ExchangeRateSourceEntity, Long> {

    List<ExchangeRateSourceEntity> findAll();

    Optional<ExchangeRateSourceEntity> findByUrl(String url);

}
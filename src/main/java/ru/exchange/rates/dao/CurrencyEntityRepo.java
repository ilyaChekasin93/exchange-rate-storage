package ru.exchange.rates.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.exchange.rates.dao.entity.CurrencyEntity;

import java.util.List;
import java.util.Optional;


@Repository
public interface CurrencyEntityRepo extends JpaRepository<CurrencyEntity, Long> {

    Optional<CurrencyEntity> findByName(String name);

    List<CurrencyEntity> findAll();

}

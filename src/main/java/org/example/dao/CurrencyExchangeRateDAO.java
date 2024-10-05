package org.example.dao;

import org.example.entities.CurrencyExchangeRateEntity;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public interface CurrencyExchangeRateDAO {
    Optional<CurrencyExchangeRateEntity> find(Integer id);

    List<CurrencyExchangeRateEntity> findAll(Integer page, Integer pageSize, String sortName, String sortOrder, AtomicLong total);
}
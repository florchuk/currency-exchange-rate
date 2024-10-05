package org.example.services;

import org.example.dao.CurrencyExchangeRateDAO;
import org.example.dto.CurrencyExchangeRateDTO;
import org.example.entities.CurrencyExchangeRateEntity;
import org.example.dao.mappings.CurrencyExchangeRateDAOMapping;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CurrencyExchangeRateService {
    private final CurrencyExchangeRateDAO currencyExchangeRateDAO;
    private final CurrencyExchangeRateDAOMapping currencyExchangeRateDAOMapping;

    public CurrencyExchangeRateService(
            CurrencyExchangeRateDAO currencyExchangeRateDAO,
            CurrencyExchangeRateDAOMapping currencyExchangeRateDAOMapping
    ) {
        this.currencyExchangeRateDAO = currencyExchangeRateDAO;
        this.currencyExchangeRateDAOMapping = currencyExchangeRateDAOMapping;
    }

    public Optional<CurrencyExchangeRateDTO> find(Integer id) {
        return this.currencyExchangeRateDAO
                .find(id)
                .map(
                        (CurrencyExchangeRateEntity currencyExchangeRateEntity) ->
                                new CurrencyExchangeRateDTO(
                                        currencyExchangeRateEntity.getId(),
                                        currencyExchangeRateEntity.getScraperEntity().getId(),
                                        currencyExchangeRateEntity.getUnit(),
                                        currencyExchangeRateEntity.getUnitCurrencyCode(),
                                        currencyExchangeRateEntity.getRateCurrencyCode(),
                                        currencyExchangeRateEntity.getBuyRate(),
                                        currencyExchangeRateEntity.getSaleRate(),
                                        currencyExchangeRateEntity.getCreatedAt(),
                                        currencyExchangeRateEntity.getUpdatedAt()
                                )
                );
    }

    public List<CurrencyExchangeRateDTO> findAll(Integer page, Integer pageSize, String sortName, String sortOrder, AtomicLong total) {
        return this.currencyExchangeRateDAO
                .findAll(page, pageSize, this.currencyExchangeRateDAOMapping.getMappingField(sortName), sortOrder, total)
                .stream()
                .map(
                        (CurrencyExchangeRateEntity currencyExchangeRateEntity) ->
                                new CurrencyExchangeRateDTO(
                                        currencyExchangeRateEntity.getId(),
                                        currencyExchangeRateEntity.getScraperEntity().getId(),
                                        currencyExchangeRateEntity.getUnit(),
                                        currencyExchangeRateEntity.getUnitCurrencyCode(),
                                        currencyExchangeRateEntity.getRateCurrencyCode(),
                                        currencyExchangeRateEntity.getBuyRate(),
                                        currencyExchangeRateEntity.getSaleRate(),
                                        currencyExchangeRateEntity.getCreatedAt(),
                                        currencyExchangeRateEntity.getUpdatedAt()
                                )
                )
                .toList();
    }
}
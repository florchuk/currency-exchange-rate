package org.example.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class CurrencyExchangeRateEntity {
    private Integer id;

    private ScraperEntity scraperEntity;

    private Integer unit;

    private String unitCurrencyCode;

    private String rateCurrencyCode;

    private Double buyRate;

    private Double saleRate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
package org.example.dao.mappings;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PostgreSQLCurrencyExchangeRateDAOMapping implements CurrencyExchangeRateDAOMapping {
    private final Map<String, String> mapping = new HashMap<>(){{
        put("id", "cer.id");
        put("scraper_id", "cer.scraper_id");
        put("unit", "cer.unit_currency_code");
        put("unit_currency_code", "cer.unit_currency_code");
        put("rate_currency_code", "cer.rate_currency_code");
        put("buy_rate", "cer.buy_rate");
        put("sale_rate", "cer.sale_rate");
        put("created_at", "cer.created_at");
        put("updated_at", "cer.updated_at");
    }};

    @Override
    public String getMappingField(String key) {
        return this.mapping.getOrDefault(key, "cer.id");
    }
}
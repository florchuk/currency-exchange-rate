package org.example.dao;

import org.example.entities.CurrencyExchangeRateEntity;
import org.example.entities.ScraperEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class PostgreSQLCurrencyExchangeRateDAO implements CurrencyExchangeRateDAO {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PostgreSQLCurrencyExchangeRateDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Optional<CurrencyExchangeRateEntity> find(Integer id) {
        return this.namedParameterJdbcTemplate
                .query("""
                        SELECT cer.scraper_id AS cer_scraper_id,
                            cer.unit AS cer_unit,
                            cer.unit_currency_code AS cer_unit_currency_code,
                            cer.rate_currency_code AS cer_rate_currency_code,
                            cer.buy_rate AS cer_buy_rate,
                            cer.sale_rate AS cer_sale_rate,
                            cer.created_at AS cer_created_at,
                            cer.updated_at AS cer_updated_at,
                            s.name_en AS s_name_en,
                            s.name_uk AS s_name_uk,
                            s.created_at AS s_created_at,
                            s.updated_at AS s_updated_at
                        FROM currency_exchange_rates cer
                        INNER JOIN scrapers s ON (
                            cer.scraper_id = s.id
                        )
                        WHERE cer.id = :id
                        LIMIT 1;
                    """,
                    new MapSqlParameterSource().addValue("id", id, Types.INTEGER),
                    (rs, rowNum) ->
                            new CurrencyExchangeRateEntity(
                                    id,
                                    new ScraperEntity(
                                            rs.getInt("cer_scraper_id"),
                                            rs.getString("s_name_en"),
                                            rs.getString("s_name_uk"),
                                            rs.getTimestamp("s_created_at").toLocalDateTime(),
                                            rs.getTimestamp("s_updated_at").toLocalDateTime()
                                    ),
                                    rs.getInt("cer_unit"),
                                    rs.getString("cer_unit_currency_code"),
                                    rs.getString("cer_rate_currency_code"),
                                    rs.getDouble("cer_buy_rate"),
                                    rs.getDouble("cer_sale_rate"),
                                    rs.getTimestamp("cer_created_at").toLocalDateTime(),
                                    rs.getTimestamp("cer_updated_at").toLocalDateTime()
                            )
                )
                .stream()
                .findFirst();
    }

    @Override
    public List<CurrencyExchangeRateEntity> findAll(
            Integer page,
            Integer pageSize,
            String sortName,
            String sortOrder,
            AtomicLong total
    ) {
        return this.namedParameterJdbcTemplate
                .query("""
                        SELECT COUNT(*) OVER() AS total,
                            cer.id AS cer_id,
                            cer.scraper_id AS cer_scraper_id,
                            cer.unit AS cer_unit,
                            cer.unit_currency_code AS cer_unit_currency_code,
                            cer.rate_currency_code AS cer_rate_currency_code,
                            cer.buy_rate AS cer_buy_rate,
                            cer.sale_rate AS cer_sale_rate,
                            cer.created_at AS cer_created_at,
                            cer.updated_at AS cer_updated_at,
                            s.name_en AS s_name_en,
                            s.name_uk AS s_name_uk,
                            s.created_at AS s_created_at,
                            s.updated_at AS s_updated_at
                        FROM currency_exchange_rates cer
                        INNER JOIN scrapers s ON (
                            cer.scraper_id = s.id
                        )
                        ORDER BY""" + " " + sortName + " " + sortOrder + """
                        LIMIT :limit OFFSET :offset;
                    """,
                    new MapSqlParameterSource()
                            .addValue("limit", pageSize, Types.INTEGER)
                            .addValue("offset", page * pageSize - pageSize, Types.BIGINT),
                    (rs, rowNum) -> {
                            CurrencyExchangeRateEntity currencyExchangeRateEntity = new CurrencyExchangeRateEntity(
                                    rs.getInt("cer_id"),
                                    new ScraperEntity(
                                            rs.getInt("cer_scraper_id"),
                                            rs.getString("s_name_en"),
                                            rs.getString("s_name_uk"),
                                            rs.getTimestamp("s_created_at").toLocalDateTime(),
                                            rs.getTimestamp("s_updated_at").toLocalDateTime()
                                    ),
                                    rs.getInt("cer_unit"),
                                    rs.getString("cer_unit_currency_code"),
                                    rs.getString("cer_rate_currency_code"),
                                    rs.getDouble("cer_buy_rate"),
                                    rs.getDouble("cer_sale_rate"),
                                    rs.getTimestamp("cer_created_at").toLocalDateTime(),
                                    rs.getTimestamp("cer_updated_at").toLocalDateTime()
                            );

                            if (rowNum == 0) {
                                total.set(rs.getLong("total"));
                            }

                            return currencyExchangeRateEntity;
                    }
                );
    }
}
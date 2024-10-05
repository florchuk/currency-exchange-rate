package org.example.dao;

import org.example.entities.ScraperEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class PostgreSQLScraperDAO implements ScraperDAO {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PostgreSQLScraperDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Optional<ScraperEntity> find(Integer id) {
        return this.namedParameterJdbcTemplate
                .query("""
                                SELECT s.name_en AS s_name_en,
                                    s.name_uk AS s_name_uk,
                                    s.created_at AS s_created_at,
                                    s.updated_at AS s_updated_at
                                FROM scrapers s
                                WHERE s.id = :id
                                LIMIT 1;
                        """,
                        new MapSqlParameterSource().addValue("id", id, Types.INTEGER),
                        (rs, rowNum) ->
                                new ScraperEntity(
                                        id,
                                        rs.getString("s_name_en"),
                                        rs.getString("s_name_uk"),
                                        rs.getTimestamp("s_created_at").toLocalDateTime(),
                                        rs.getTimestamp("s_updated_at").toLocalDateTime()
                                )
                )
                .stream()
                .findFirst();
    }

    @Override
    public List<ScraperEntity> findAll(Integer page, Integer pageSize, String sortName, String sortOrder, AtomicLong total) {
        return this.namedParameterJdbcTemplate
                .query("""
                            SELECT COUNT(*) OVER() AS total,
                                s.id AS s_id,
                                s.name_en AS s_name_en,
                                s.name_uk AS s_name_uk,
                                s.created_at AS s_created_at,
                                s.updated_at AS s_updated_at
                            FROM scrapers s
                            ORDER BY""" + " " + sortName + " " + sortOrder + """
                            LIMIT :limit OFFSET :offset;
                    """,
                    new MapSqlParameterSource()
                            .addValue("limit", pageSize, Types.INTEGER)
                            .addValue("offset", page * pageSize - pageSize, Types.BIGINT),
                    (rs, rowNum) -> {
                        ScraperEntity scraperEntity = new ScraperEntity(
                                rs.getInt("s_id"),
                                rs.getString("s_name_en"),
                                rs.getString("s_name_uk"),
                                rs.getTimestamp("s_created_at").toLocalDateTime(),
                                rs.getTimestamp("s_updated_at").toLocalDateTime()
                        );

                        if (rowNum == 0) {
                            total.set(rs.getLong("total"));
                        }

                        return scraperEntity;
                    }
                );
    }
}
package org.example.dao.mappings;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PostgreSQLScraperDAOMapping implements ScraperDAOMapping {
    private final Map<String, String> mapping = new HashMap<>(){{
        put("id", "s.id");
        put("name_en", "s.name_en");
        put("name_uk", "s.name_uk");
        put("created_at", "s.created_at");
        put("updated_at", "s.updated_at");
    }};

    @Override
    public String getMappingField(String key) {
        return this.mapping.getOrDefault(key, "s.id");
    }
}
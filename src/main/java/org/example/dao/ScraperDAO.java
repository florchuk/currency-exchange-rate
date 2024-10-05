package org.example.dao;

import org.example.entities.ScraperEntity;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public interface ScraperDAO {
    Optional<ScraperEntity> find(Integer id);

    List<ScraperEntity> findAll(Integer page, Integer pageSize, String sortName, String sortOrder, AtomicLong total);
}

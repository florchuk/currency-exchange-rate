package org.example.services;

import org.example.dao.ScraperDAO;
import org.example.dto.ScraperDTO;
import org.example.entities.ScraperEntity;
import org.example.dao.mappings.ScraperDAOMapping;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ScraperService {
    private final ScraperDAO scraperDAO;
    private final ScraperDAOMapping scraperDAOMapping;

    public ScraperService(ScraperDAO scraperDAO, ScraperDAOMapping scraperDAOMapping) {
        this.scraperDAO = scraperDAO;
        this.scraperDAOMapping = scraperDAOMapping;
    }

    public Optional<ScraperDTO> find(Integer id) {
        return this.scraperDAO
                .find(id)
                .map(
                    (ScraperEntity scraperEntity) ->
                            new ScraperDTO(
                                    scraperEntity.getId(),
                                    scraperEntity.getNameEn(),
                                    scraperEntity.getNameUk(),
                                    scraperEntity.getCreatedAt(),
                                    scraperEntity.getUpdatedAt()
                            )
                );
    }

    public List<ScraperDTO> findAll(Integer page, Integer pageSize, String sortName, String sortOrder, AtomicLong total) {
        return this.scraperDAO
                .findAll(page, pageSize, this.scraperDAOMapping.getMappingField(sortName), sortOrder, total)
                .stream()
                .map(
                        (ScraperEntity scraperEntity) ->
                                new ScraperDTO(
                                        scraperEntity.getId(),
                                        scraperEntity.getNameEn(),
                                        scraperEntity.getNameUk(),
                                        scraperEntity.getCreatedAt(),
                                        scraperEntity.getUpdatedAt()
                                )
                )
                .toList();
    }
}
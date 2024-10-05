package org.example.controllers;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.example.dto.ScraperDTO;
import org.example.services.ScraperService;
import org.example.utils.Content;
import org.example.utils.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Validated
@RequestMapping(path = "/api")
@RestController
public class ScraperController {
    private final ScraperService scraperService;

    public ScraperController(ScraperService scraperService) {
        this.scraperService = scraperService;
    }

    @GetMapping(path = "/scrapers")
    public ResponseEntity<Page<ScraperDTO>> getScrapers(
            @RequestParam(name = "page", required = false, defaultValue = "1") @Positive Integer page,
            @RequestParam(name = "page_size", required = false, defaultValue = "50") @Positive @Max(value = 50) Integer pageSize,
            @RequestParam(name = "sort_name", required = false, defaultValue = "id") @Pattern(regexp = "^(id|name_en|name_uk|created_at|updated_at)$") String sortName,
            @RequestParam(name = "sort_order", required = false, defaultValue = "asc") @Pattern(regexp = "^(asc|desc)$") String sortOrder
    ) {
        AtomicLong total = new AtomicLong();
        List<ScraperDTO> scraperDTOs = this.scraperService.findAll(page, pageSize, sortName, sortOrder, total);
        Page<ScraperDTO> pageScraperDTOs = new Page<>(
                scraperDTOs,
                page,
                pageSize,
                sortName,
                sortOrder,
                total.get()
        );

        return new ResponseEntity<>(pageScraperDTOs, HttpStatus.OK);
    }

    @GetMapping(path = "/scrapers/{id}")
    public ResponseEntity<Content<ScraperDTO>> getScrapers(@PathVariable(name = "id") @Positive Integer id) {
        Optional<ScraperDTO> optionalScraperDTO = this.scraperService.find(id);

        return optionalScraperDTO.isPresent()
                ? new ResponseEntity<>(new Content<>(optionalScraperDTO.stream().toList()), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
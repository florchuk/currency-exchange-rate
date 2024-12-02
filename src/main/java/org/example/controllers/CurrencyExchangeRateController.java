package org.example.controllers;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.example.dto.CurrencyExchangeRateDTO;
import org.example.exceptions.CurrencyExchangeRateNotFoundException;
import org.example.services.CurrencyExchangeRateService;
import org.example.utils.Content;
import org.example.utils.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RequestMapping(path = "/api/currency-exchange-rates")
@RestController
public class CurrencyExchangeRateController {
    private final CurrencyExchangeRateService currencyExchangeRateService;

    public CurrencyExchangeRateController(CurrencyExchangeRateService currencyExchangeRateService) {
        this.currencyExchangeRateService = currencyExchangeRateService;
    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<Page<CurrencyExchangeRateDTO>> getCurrencyExchangeRates(
            @RequestParam(name = "page", required = false, defaultValue = "1") @Positive Integer page,
            @RequestParam(name = "page_size", required = false, defaultValue = "50") @Positive @Max(value = 50) Integer pageSize,
            @RequestParam(name = "sort_name", required = false, defaultValue = "id") @Pattern(regexp = "^(id|scraper_id|unit|unit_currency_code|rate_currency_code|buy_rate|sale_rate|created_at|updated_at)$") String sortName,
            @RequestParam(name = "sort_order", required = false, defaultValue = "asc") @Pattern(regexp = "^(asc|desc)$") String sortOrder
    ) {
        AtomicLong total = new AtomicLong();
        List<CurrencyExchangeRateDTO> currencyExchangeRateDTOs =
                this.currencyExchangeRateService.findAll(page, pageSize, sortName, sortOrder, total);
        Page<CurrencyExchangeRateDTO> pageCurrencyExchangeRateDTOs = new Page<>(
                currencyExchangeRateDTOs,
                page,
                pageSize,
                sortName,
                sortOrder,
                total.get()
        );

        return new ResponseEntity<>(pageCurrencyExchangeRateDTOs, HttpStatus.OK);
    }

    @GetMapping(path = {"/{id}", "/{id}/"})
    public ResponseEntity<Content<CurrencyExchangeRateDTO>> getCurrencyExchangeRates(
            @PathVariable(name = "id") @Positive Integer id
    ) throws CurrencyExchangeRateNotFoundException {
        Optional<CurrencyExchangeRateDTO> optionalCurrencyExchangeRateDTO = this.currencyExchangeRateService.find(id);

        if (optionalCurrencyExchangeRateDTO.isPresent()) {
            return ResponseEntity.ok(new Content<>(optionalCurrencyExchangeRateDTO.stream().toList()));
        } else {
            throw new CurrencyExchangeRateNotFoundException();
        }
    }
}
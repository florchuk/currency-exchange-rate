package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class ScraperDTO {
    private Integer id;

    private String nameEn;

    private String nameUk;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
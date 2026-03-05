package com.github.frederikpietzko.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record CreateVisitDto(
  @NotEmpty String description,
  @NotNull CreatePetDto pet,
  @NotNull OffsetDateTime dateTime
) {
}

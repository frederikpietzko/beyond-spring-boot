package com.github.frederikpietzko.dto;

import com.github.frederikpietzko.model.Visit;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record CreateVisitDto(
  @NotEmpty String description,
  @NotNull CreatePetDto pet,
  @NotNull OffsetDateTime dateTime
) {

  public Visit toVisitEntity() {
    return new Visit(null, description, pet.toEntity(), dateTime);
  }
}

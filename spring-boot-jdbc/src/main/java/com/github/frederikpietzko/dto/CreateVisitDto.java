package com.github.frederikpietzko.dto;

import com.github.frederikpietzko.model.VisitEntity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record CreateVisitDto(
  @NotEmpty String description,
  @NotNull CreatePetDto pet,
  @NotNull OffsetDateTime dateTime
) {

  public VisitEntity toVisitEntity() {
    return new VisitEntity(description, pet.toEntity(), dateTime);
  }
}

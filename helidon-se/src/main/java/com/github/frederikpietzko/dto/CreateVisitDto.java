package com.github.frederikpietzko.dto;

import com.github.frederikpietzko.model.Visit;

import java.time.OffsetDateTime;

public record CreateVisitDto(
  String description,
  CreatePetDto pet,
  OffsetDateTime dateTime
) {

  public Visit toVisit() {
    return new Visit(null, description, pet.toEntity(), dateTime);
  }
}

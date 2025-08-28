package com.github.frederikpietzko.dto;

import com.github.frederikpietzko.model.Visit;

import java.time.ZonedDateTime;

public record CreateVisitDto(
  String description,
  CreatePetDto pet,
  ZonedDateTime dateTime
) {

  public Visit toVisit() {
    return new Visit(null, description, pet.toEntity(), dateTime);
  }
}

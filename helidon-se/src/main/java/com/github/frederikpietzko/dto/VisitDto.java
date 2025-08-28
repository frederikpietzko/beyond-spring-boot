package com.github.frederikpietzko.dto;

import com.github.frederikpietzko.model.Visit;

import java.time.ZonedDateTime;

public record VisitDto(
  Long id,
  String description,
  PetDto pet,
  ZonedDateTime dateTime
) {

  public static VisitDto fromEntity(Visit entity) {
    return new VisitDto(
      entity.id(),
      entity.description(),
      PetDto.fromEntity(entity.pet()),
      entity.dateTime()
    );
  }
}

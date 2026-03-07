package com.github.frederikpietzko.dto;

import com.github.frederikpietzko.model.Visit;

import java.time.OffsetDateTime;

public record VisitDto(
  Long id,
  String description,
  PetDto pet,
  OffsetDateTime dateTime
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

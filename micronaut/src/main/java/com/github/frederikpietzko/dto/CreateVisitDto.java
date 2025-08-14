package com.github.frederikpietzko.dto;

import com.github.frederikpietzko.model.VisitEntity;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

@Serdeable.Deserializable
public record CreateVisitDto(
  @NotEmpty String description,
  @NotNull CreatePetDto pet,
  @NotNull OffsetDateTime dateTime
) {

  public VisitEntity toVisitEntity() {
    return new VisitEntity(description, pet.toEntity(), dateTime);
  }
}

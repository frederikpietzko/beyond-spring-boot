package com.github.frederikpietzko.model;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedEntity("visit")
public class VisitEntity {
  @Id
  @GeneratedValue(GeneratedValue.Type.IDENTITY)
  public Long id;
  @NotEmpty
  public String description;
  @NotNull
  @MappedProperty("pet_id")
  public Long petId;
  @NotNull
  public OffsetDateTime dateTime;

  public VisitEntity(String description, Long petId, OffsetDateTime dateTime) {
    this.description = description;
    this.petId = petId;
    this.dateTime = dateTime;
  }
}

package com.github.frederikpietzko.model;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
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
  @GeneratedValue
  public Long id;
  @NotEmpty
  public String description;
  @NotNull
  @Relation(Relation.Kind.MANY_TO_ONE)
  public PetEntity pet;
  @NotNull
  public OffsetDateTime dateTime;

}

package com.github.frederikpietzko.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Table(name = "visit")
public class VisitEntity extends BaseEntity {
  @NotEmpty
  public String description;
  @NotNull
  @MappedCollection(idColumn = "pet_id")
  public PetEntity pet;
  @NotNull
  public OffsetDateTime dateTime;
}

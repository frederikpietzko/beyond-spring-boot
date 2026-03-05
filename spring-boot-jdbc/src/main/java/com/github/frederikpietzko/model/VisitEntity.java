package com.github.frederikpietzko.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;

@NoArgsConstructor
@Table(name = "visit")
public class VisitEntity extends BaseEntity {
  @NotEmpty
  public String description;

  @NotNull
  @Column("pet_id")
  public Long petId;

  @NotNull
  @Column("date_time")
  public OffsetDateTime dateTime;

  public VisitEntity(String description, Long petId, OffsetDateTime dateTime) {
    this.description = description;
    this.petId = petId;
    this.dateTime = dateTime;
  }
}

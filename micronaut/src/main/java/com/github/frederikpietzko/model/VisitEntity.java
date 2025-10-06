package com.github.frederikpietzko.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "visit")
public class VisitEntity extends BaseEntity {
  @NotEmpty
  public String description;
  @NotNull
  @ManyToOne(
    optional = false,
    cascade = CascadeType.ALL
  )
  public PetEntity pet;
  @NotNull
  @Column(name = "date_time")
  public OffsetDateTime dateTime;

}

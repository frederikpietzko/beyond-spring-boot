package com.github.frederikpietzko.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class VisitEntity extends PanacheEntity {
  @NotEmpty
  public String description;
  @NotNull
  @ManyToOne(
    optional = false,
    cascade = CascadeType.ALL
  )
  public PetEntity pet;
  @NotNull
  public OffsetDateTime dateTime;

}

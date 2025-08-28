package com.github.frederikpietzko.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pet")
public class PetEntity extends PanacheEntity {
  @NotEmpty
  public String name;
  @Min(1)
  public int age;
  @NotNull
  @Enumerated(EnumType.STRING)
  public Type type;
}

package com.github.frederikpietzko.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PetEntity extends BaseEntity {
  @NotEmpty
  public String name;
  @Min(1)
  public int age;
  @NotNull
  @Enumerated(EnumType.STRING)
  public Type type;
}

package com.github.frederikpietzko.model;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedEntity("pet")
public class PetEntity {
  @Id
  @GeneratedValue(GeneratedValue.Type.IDENTITY)
  public Long id;
  @NotEmpty
  public String name;
  @Min(1)
  public int age;
  @NotNull
  public Type type;
}

package com.github.frederikpietzko.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pet")
public class PetEntity extends BaseEntity {
  @NotEmpty
  public String name;
  @Min(1)
  public int age;
  @NotNull
  public Type type;
}

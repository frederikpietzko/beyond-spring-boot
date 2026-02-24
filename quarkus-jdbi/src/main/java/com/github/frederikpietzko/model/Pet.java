package com.github.frederikpietzko.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public final class Pet {
  private Long id;
  private String name;
  private Integer age;
  private Type type;
}

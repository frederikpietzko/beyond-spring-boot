package com.github.frederikpietzko.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public final class Visit {
  private Long id;
  private String description;
  private Pet pet;
  private OffsetDateTime dateTime;
}

package com.github.frederikpietzko.model;

import java.time.OffsetDateTime;

public record Visit(
  Long id,
  String description,
  Pet pet,
  OffsetDateTime dateTime
) {
}

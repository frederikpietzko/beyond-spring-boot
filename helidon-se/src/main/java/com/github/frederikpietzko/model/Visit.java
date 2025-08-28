package com.github.frederikpietzko.model;

import java.time.ZonedDateTime;

public record Visit(
  Long id,
  String description,
  Pet pet,
  ZonedDateTime dateTime
) {
}

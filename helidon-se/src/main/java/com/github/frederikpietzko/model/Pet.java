package com.github.frederikpietzko.model;

public record Pet(
  Long id,
  String name,
  int age,
  Type type
) {
}

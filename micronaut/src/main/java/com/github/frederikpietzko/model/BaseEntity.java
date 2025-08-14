package com.github.frederikpietzko.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {
  @Id
  @GeneratedValue
  public Long id;
}

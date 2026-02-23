package com.github.frederikpietzko.model;


import org.springframework.data.annotation.Id;

public abstract class BaseEntity {
  @Id
  public Long id;
}

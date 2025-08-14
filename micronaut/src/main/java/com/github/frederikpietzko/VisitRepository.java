package com.github.frederikpietzko;

import com.github.frederikpietzko.model.VisitEntity;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@Singleton
@RequiredArgsConstructor
public class VisitRepository {
  private final EntityManager entityManager;

  public Stream<VisitEntity> streamAll() {
    return entityManager.createQuery("from VisitEntity", VisitEntity.class)
      .getResultStream();
  }

  public VisitEntity findById(Long id) {
    return entityManager.find(VisitEntity.class, id);
  }

  public VisitEntity save(VisitEntity visitEntity) {
    entityManager.persist(visitEntity);
    return visitEntity;
  }
}

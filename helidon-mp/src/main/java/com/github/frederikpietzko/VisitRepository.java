package com.github.frederikpietzko;

import com.github.frederikpietzko.model.VisitEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.stream.Stream;

@ApplicationScoped
public class VisitRepository {
  @PersistenceContext(unitName = "default")
  private EntityManager entityManager;


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

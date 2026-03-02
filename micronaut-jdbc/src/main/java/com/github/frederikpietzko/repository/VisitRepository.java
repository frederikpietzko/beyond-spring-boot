package com.github.frederikpietzko.repository;

import com.github.frederikpietzko.model.VisitEntity;
import io.micronaut.data.annotation.Join;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface VisitRepository extends CrudRepository<VisitEntity, Long> {

  @Join(value = "pet", type = Join.Type.FETCH)
  @Override
  List<VisitEntity> findAll();

  @Join(value = "pet", type = Join.Type.FETCH)
  @Override
  Optional<VisitEntity> findById(Long id);
}

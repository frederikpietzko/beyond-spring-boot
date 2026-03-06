package com.github.frederikpietzko.repository;

import com.github.frederikpietzko.dto.PetDto;
import com.github.frederikpietzko.dto.VisitDto;
import com.github.frederikpietzko.model.Type;
import com.github.frederikpietzko.model.VisitEntity;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import io.micronaut.serde.annotation.Serdeable;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface VisitRepository extends CrudRepository<VisitEntity, Long> {

  @Query("""
    SELECT v.id as visit_id, v.description, v.date_time, v.pet_id, p.name as pet_name, p.age as pet_age, p.type as pet_type
    FROM visit v
    JOIN pet p ON v.pet_id = p.id
    """)
  List<VisitProjection> findAllWithPet();

  @Query("""
    SELECT v.id as visit_id, v.description, v.date_time, v.pet_id, p.name as pet_name, p.age as pet_age, p.type as pet_type
    FROM visit v
    JOIN pet p ON v.pet_id = p.id
    WHERE v.id = :id
    """)
  Optional<VisitProjection> findByIdWithPet(Long id);

  @Serdeable
  record VisitProjection(
    Long visit_id,
    String description,
    OffsetDateTime date_time,
    Long pet_id,
    String pet_name,
    int pet_age,
    Type pet_type
  ) {
    public VisitDto toDto() {
      return new VisitDto(
        visit_id,
        description,
        new PetDto(pet_id, pet_name, pet_age, pet_type),
        date_time
      );
    }
  }
}

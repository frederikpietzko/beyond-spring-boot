package com.github.frederikpietzko;

import com.github.frederikpietzko.model.Type;
import com.github.frederikpietzko.model.VisitEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface VisitRepository extends CrudRepository<VisitEntity, Long> {

  @Query("""
    SELECT v.id as visit_id, v.description, v.date_time,
    v.pet_id, p.name as pet_name, p.age as pet_age, p.type as pet_type
    FROM visit v
    JOIN pet p ON v.pet_id = p.id
    """)
  List<VisitProjection> findAllWithPet();

  record VisitProjection(
    Long visit_id,
    String description,
    OffsetDateTime date_time,
    Long pet_id,
    String pet_name,
    int pet_age,
    Type pet_type
  ) {
  }

  @Query("""
    SELECT v.id as visit_id, v.description, v.date_time, v.pet_id, p.name as pet_name, p.age as pet_age, p.type as pet_type
    FROM visit v
    JOIN pet p ON v.pet_id = p.id
    WHERE v.id = :id
    """)
  Optional<VisitProjection> findByIdWithPet(Long id);
}

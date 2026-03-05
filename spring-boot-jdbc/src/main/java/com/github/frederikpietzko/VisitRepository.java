package com.github.frederikpietzko;

import com.github.frederikpietzko.model.Type;
import com.github.frederikpietzko.model.VisitEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.repository.CrudRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

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

  interface VisitProjection {
    @Column("visit_id")
    Long getId();

    @Column("description")
    String getDescription();

    @Column("date_time")
    OffsetDateTime getDateTime();

    @Column("pet_id")
    Long getPetId();

    @Column("pet_name")
    String getPetName();

    @Column("pet_age")
    int getPetAge();

    @Column("pet_type")
    Type getPetType();
  }
}

package com.github.frederikpietzko;

import com.github.frederikpietzko.model.Pet;
import com.github.frederikpietzko.model.Visit;
import org.jdbi.v3.core.mapper.JoinRow;
import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterJoinRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RegisterBeanMapper(value = Visit.class, prefix = "v")
@RegisterBeanMapper(value = Pet.class, prefix = "p")
public interface VisitRepository extends SqlObject {
  String SELECT_FIND_ALL = """
    select v.id v_id,
     v.description v_description,
     v.date_time v_date_time,
     v.pet_id v_pet_id,
     p.id p_id,
     p.name p_name,
     p.age p_age,
    p.type p_type
     FROM visit v INNER JOIN pet p ON v.pet_id = p.id
    ;""";

  String SELCT_FIND_BY_ID = """
      select v.id v_id,
       v.description v_description,
       v.date_time v_date_time,
       v.pet_id v_pet_id,
       p.id p_id,
       p.name p_name,
       p.age p_age,
       p.type p_type
       FROM visit v INNER JOIN pet p ON v.pet_id = p.id
       WHERE v.id = :id
    """;


  @SqlQuery(SELECT_FIND_ALL)
  @RegisterJoinRowMapper({Visit.class, Pet.class})
  List<JoinRow> findAllRaw();

  default Stream<Visit> findAll() {
    return findAllRaw()
      .stream()
      .map(row -> {
        final var visit = row.get(Visit.class);
        final var pet = row.get(Pet.class);
        visit.setPet(pet);
        return visit;
      });
  }

  @SqlQuery(SELCT_FIND_BY_ID)
  @RegisterJoinRowMapper({Visit.class, Pet.class})
  Optional<JoinRow> findByIdRaw(@Bind("id") Long id);

  default Optional<Visit> findById(Long id) {
    return findByIdRaw(id)
      .map(row -> {
        final var visit = row.get(Visit.class);
        final var pet = row.get(Pet.class);
        visit.setPet(pet);
        return visit;
      });
  }

  @SqlUpdate("INSERT INTO visit (description, date_time, pet_id) VALUES (:description, :dateTime, :petId)")
  @GetGeneratedKeys
  Long insertVisit(@BindBean Visit visit, @Bind("petId") Long petId);

  @SqlUpdate("INSERT INTO pet (name, age, type) VALUES (:name, :age, :type)")
  @GetGeneratedKeys
  Long insertPet(@BindBean Pet pet);
}

package com.github.frederikpietzko;

import com.github.frederikpietzko.model.Pet;
import com.github.frederikpietzko.model.Type;
import com.github.frederikpietzko.model.Visit;
import io.helidon.dbclient.DbClient;
import io.helidon.dbclient.DbRow;
import io.helidon.dbclient.DbTransaction;
import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
public class VisitRepository {
  private final DbClient dbClient;

  public List<Visit> getVisits() {
    return dbClient.execute()
      .query("select * from visit join pet on visit.pet_id = pet.id")
      .map(this::map)
      .toList();
  }

  public Optional<Visit> findVisitById(Long id) {
    return dbClient
      .execute()
      .query("select * from visit join pet on visit.pet_id = pet.id where visit.id = ?", id)
      .map(this::map)
      .findAny();
  }

  public Visit save(Visit visit) {
    return insert(visit);
  }

  private Visit insert(Visit visit) {
    return transactional(tx -> {
      final var petId = Optional
        .ofNullable(visit.pet().id())
        .orElseGet(() -> {
          return tx.query(
              "insert into pet (id, name, age, type) values (nextval('pet_seq'), ?, ?, ?) returning id",
              visit.pet().name(),
              visit.pet().age(),
              visit.pet().type().name()
            )
            .findFirst()
            .map(row -> row.column("id").asLong().orElseThrow())
            .orElseThrow();
        });
      return tx.query("insert into visit (id, description, pet_id, datetime) values (nextval('visit_seq'), ?, ?, ?) returning id",
          visit.description(),
          petId,
          visit.dateTime()
        )
        .findFirst()
        .map(row -> row.column("id").asLong().orElseThrow());
    })
      .flatMap(this::findVisitById)
      .orElseThrow();
  }

  private <T> T transactional(Function<DbTransaction, T> supplier) {
    final var transaction = dbClient.transaction();
    try {
      final var result = supplier.apply(transaction);
      transaction.commit();
      return result;
    } catch (Throwable t) {
      transaction.rollback();
      throw t;
    }
  }

  private Visit map(DbRow row) {
    return new Visit(
      row.column("id").asLong().orElseThrow(),
      row.column("description").asString().orElseThrow(),
      mapPet(row),
      row.column("date_time").as(ZonedDateTime.class).orElseThrow()
    );
  }

  private Pet mapPet(DbRow row) {
    return new Pet(
      row.column("pet_id").asLong().orElseThrow(),
      row.column("name").asString().orElseThrow(),
      row.column("age").asInt().orElseThrow(),
      row.column("type").asString().map(Type::valueOf).orElseThrow()
    );
  }
}

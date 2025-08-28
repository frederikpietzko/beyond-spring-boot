package com.github.frederikpietzko;

import com.github.frederikpietzko.dto.CreateVisitDto;
import com.github.frederikpietzko.dto.VisitDto;
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.HttpService;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;

import java.util.Optional;

public class VisitService implements HttpService {

  private final VisitRepository visitRepository;

  VisitService(ApplicationContext applicationContext) {
    this.visitRepository = applicationContext.getVisitRepository();
  }

  @Override
  public void routing(HttpRules httpRules) {
    httpRules
      .get("/", this::getVisits)
      .post("/", this::createVisit)
      .get("/:id", this::getVisit);
  }

  private void getVisits(ServerRequest req, ServerResponse res) {
    final var visits = visitRepository
      .getVisits()
      .stream()
      .map(VisitDto::fromEntity)
      .toList();
    res.send(visits);
  }

  private void getVisit(ServerRequest req, ServerResponse res) {
    Optional
      .of(Long.getLong(req.path().pathParameters().get("id")))
      .flatMap(visitRepository::findVisitById)
      .map(VisitDto::fromEntity)
      .ifPresentOrElse(
        res::send,
        () -> res.status(404).send("Visit not found")
      );
  }

  private void createVisit(ServerRequest req, ServerResponse res) {
    final var createVisit = req.content().as(CreateVisitDto.class);
    final var visit = visitRepository.save(createVisit.toVisit());
    res.status(201)
      .send(VisitDto.fromEntity(visit));
  }
}

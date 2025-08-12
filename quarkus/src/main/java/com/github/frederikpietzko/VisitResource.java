package com.github.frederikpietzko;

import com.github.frederikpietzko.dto.CreateVisitDto;
import com.github.frederikpietzko.dto.VisitDto;
import com.github.frederikpietzko.model.VisitEntity;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.stream.Stream;

@Path("/visits")
@Transactional
public class VisitResource {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<VisitDto> getVisits() {
    final Stream<VisitEntity> visits = VisitEntity
      .streamAll();

    return visits
      .map(VisitDto::fromEntity)
      .toList();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public VisitDto getVisit(@NotNull @Min(1) Long id) {
    final VisitEntity visit = VisitEntity
      .findById(id);

    if (visit == null) {
      throw new NotFoundException("Visit not found");
    }

    return VisitDto.fromEntity(visit);
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public VisitDto createVisit(CreateVisitDto visit) {
    final var newVisit = visit.toVisitEntity();
    newVisit.persist();
    return VisitDto.fromEntity(newVisit);
  }
}


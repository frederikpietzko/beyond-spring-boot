package com.github.frederikpietzko;

import com.github.frederikpietzko.dto.CreateVisitDto;
import com.github.frederikpietzko.dto.VisitDto;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/visits")
@Transactional
public class VisitResource {
  @Inject
  VisitRepository visitRepository;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<VisitDto> getAllVisits() {
    return visitRepository.findAll().map(VisitDto::fromEntity).toList();
  }


  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public VisitDto getVisit(@NotNull @Min(1) Long id) {
    return visitRepository
      .findById(id)
      .map(VisitDto::fromEntity)
      .orElseThrow(() -> new NotFoundException("Visit not found"));
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public VisitDto createVisit(@NotNull @Valid CreateVisitDto visitDto) {
    final var visit = visitDto.toVisitEntity();
    final var pet = visit.getPet();
    if (pet.getId() == null) {
      final var insertedPetId = visitRepository.insertPet(pet);
      pet.setId(insertedPetId);
    }
    visitRepository.insertVisit(visit, pet.getId());
    return VisitDto.fromEntity(visit);
  }
}

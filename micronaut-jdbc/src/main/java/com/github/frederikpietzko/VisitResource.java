package com.github.frederikpietzko;

import com.github.frederikpietzko.dto.CreateVisitDto;
import com.github.frederikpietzko.dto.PetDto;
import com.github.frederikpietzko.dto.VisitDto;
import com.github.frederikpietzko.model.VisitEntity;
import com.github.frederikpietzko.repository.PetRepository;
import com.github.frederikpietzko.repository.VisitRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Path("/visits")
@Transactional
@RequiredArgsConstructor
public class VisitResource {
  private final VisitRepository visitRepository;
  private final PetRepository petRepository;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<VisitDto> getVisits() {
    return visitRepository.findAllWithPet().stream().map(VisitRepository.VisitProjection::toDto)
      .toList();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public VisitDto getVisit(@NotNull @Min(1) Long id) {
    return visitRepository.findByIdWithPet(id).map(VisitRepository.VisitProjection::toDto)
      .orElseThrow(() -> new NotFoundException("Visit not found"));
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public VisitDto createVisit(CreateVisitDto visitDto) {
    final var pet = petRepository.save(visitDto.pet().toEntity());
    final var visit = new VisitEntity(visitDto.description(), pet.getId(), visitDto.dateTime());
    final var entity = visitRepository.save(visit);
    return VisitDto.fromEntity(entity, PetDto.fromEntity(pet));
  }
}


package com.github.frederikpietzko;

import com.github.frederikpietzko.dto.CreateVisitDto;
import com.github.frederikpietzko.dto.PetDto;
import com.github.frederikpietzko.dto.VisitDto;
import com.github.frederikpietzko.model.VisitEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/visits")
@RequiredArgsConstructor
public class VisitController {
  private final VisitRepository visitRepository;
  private final PetRepository petRepository;

  @GetMapping
  public ResponseEntity<List<VisitDto>> getVisits() {
    final var dtos = visitRepository.findAllWithPet().stream()
      .map(this::toDto)
      .toList();
    return ResponseEntity.ok(dtos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getVisitById(@PathVariable Long id) {
    return visitRepository.findByIdWithPet(id)
      .map(this::toDto)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  @Transactional
  public ResponseEntity<?> createVisit(@RequestBody @Valid CreateVisitDto visitDto) {
    final var pet = petRepository.save(visitDto.pet().toEntity());
    final var visit = new VisitEntity(visitDto.description(), pet.id, visitDto.dateTime());
    final var entity = visitRepository.save(visit);
    return ResponseEntity.ok(VisitDto.fromEntity(entity, pet));
  }

  private VisitDto toDto(VisitRepository.VisitProjection p) {
    return new VisitDto(
      p.getId(),
      p.getDescription(),
      new PetDto(p.getPetId(), p.getPetName(), p.getPetAge(), p.getPetType()),
      p.getDateTime()
    );
  }
}

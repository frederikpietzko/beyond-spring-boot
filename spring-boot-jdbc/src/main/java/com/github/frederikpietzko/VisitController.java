package com.github.frederikpietzko;

import com.github.frederikpietzko.dto.CreateVisitDto;
import com.github.frederikpietzko.dto.VisitDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/visits")
@RequiredArgsConstructor
public class VisitController {
  private final VisitRepository visitRepository;
  private final PetRepository petRepository;

  @GetMapping
  public ResponseEntity<List<VisitDto>> getVisits() {
    final var dtos = new ArrayList<VisitDto>();
    visitRepository.findAll().forEach(visit -> dtos.add(VisitDto.fromEntity(visit)));
    return ResponseEntity.ok(dtos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getVisitById(@PathVariable Long id) {
    return visitRepository
      .findById(id)
      .map(VisitDto::fromEntity)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  @Transactional
  public ResponseEntity<?> createVisit(@RequestBody @Valid CreateVisitDto visitDto) {
    final var visit = visitDto.toVisitEntity();
    visit.pet = petRepository.save(visit.pet);
    final var entity = visitRepository.save(visit);
    return ResponseEntity.ok(VisitDto.fromEntity(entity));
  }
}

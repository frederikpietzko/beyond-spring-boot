package com.github.frederikpietzko;

import com.github.frederikpietzko.dto.CreateVisitDto;
import com.github.frederikpietzko.dto.VisitDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/visits")
@RequiredArgsConstructor
public class VisitController {
  private final JpaVisitRepository jpaVisitRepository;

  @GetMapping
  public ResponseEntity<List<VisitDto>> getVisits() {
    return ResponseEntity.ok(
      jpaVisitRepository
        .findAll()
        .stream()
        .map(VisitDto::fromEntity)
        .toList()
    );
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getVisitById(@PathVariable Long id) {
    return jpaVisitRepository
      .findById(id)
      .map(VisitDto::fromEntity)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<?> createVisit(@RequestBody @Valid CreateVisitDto visit) {
    var entity = jpaVisitRepository.save(visit.toVisitEntity());
    return ResponseEntity.ok(VisitDto.fromEntity(entity));
  }
}

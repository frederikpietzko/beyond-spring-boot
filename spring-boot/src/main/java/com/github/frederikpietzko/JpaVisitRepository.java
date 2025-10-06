package com.github.frederikpietzko;

import com.github.frederikpietzko.model.VisitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaVisitRepository extends JpaRepository<VisitEntity, Long> {
}

package com.github.frederikpietzko;

import com.github.frederikpietzko.model.VisitEntity;
import org.springframework.data.repository.CrudRepository;

public interface VisitRepository extends CrudRepository<VisitEntity, Long> {
}

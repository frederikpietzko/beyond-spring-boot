package com.github.frederikpietzko;

import com.github.frederikpietzko.model.PetEntity;
import org.springframework.data.repository.CrudRepository;

public interface PetRepository extends CrudRepository<PetEntity, Long> {
}

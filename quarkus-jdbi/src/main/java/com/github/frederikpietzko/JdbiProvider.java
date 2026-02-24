package com.github.frederikpietzko;

import io.agroal.api.AgroalDataSource;
import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

@ApplicationScoped
public class JdbiProvider {
  @Inject
  AgroalDataSource dataSource;

  @Singleton
  @Produces
  @DefaultBean
  public Jdbi jdbi() {
    final var jdbi = Jdbi.create(dataSource);
    jdbi.installPlugin(new SqlObjectPlugin());
    return jdbi;
  }

  @ApplicationScoped
  @Produces
  public VisitRepository visitRepository(Jdbi jdbi) {
    return jdbi.onDemand(VisitRepository.class);
  }

}

package com.github.frederikpietzko;

import io.helidon.dbclient.DbClient;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApplicationContext {
  private DbClient dbClient;
  private VisitRepository visitRepository;
}

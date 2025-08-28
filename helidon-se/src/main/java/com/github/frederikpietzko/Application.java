package com.github.frederikpietzko;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.helidon.config.Config;
import io.helidon.dbclient.DbClient;
import io.helidon.dbclient.DbMapper;
import io.helidon.dbclient.spi.DbMapperProvider;
import io.helidon.http.media.jackson.JacksonSupport;
import io.helidon.logging.common.LogConfig;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.HttpRouting;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class Application {
  private Application() {
  }

  public static void main(String[] args) {
    LogConfig.configureRuntime();

    final var global = Config.global();

    final var applicationContext = setupApplicationContext(global);

    final var server = WebServer.builder()
      .config(global.get("server"))
      .mediaContext(it ->
        it.mediaSupportsDiscoverServices(false)
          .addMediaSupport(JacksonSupport.create(createObjectMapper()))
          .build()
      )
      .routing(routing -> routing(routing, applicationContext))
      .build()
      .start();
    log.info("WEB Server started at http://localhost:{}", server.port());
  }

  static ObjectMapper createObjectMapper() {
    return JsonMapper.builder()
      .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
      .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
      .build()
      .registerModule(new Jdk8Module())
      .registerModule(new JavaTimeModule())
      .registerModule(new ParameterNamesModule())
      .setDefaultPropertyInclusion(
        com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
      );
  }


  static ApplicationContext setupApplicationContext(Config global) {
    final var dbClient = DbClient.create(global.get("db"));
    return ApplicationContext.builder()
      .dbClient(dbClient)
      .visitRepository(new VisitRepository(dbClient))
      .build();
  }

  static void routing(HttpRouting.Builder routing, ApplicationContext applicationContext) {
    routing
      .register("/visits", new VisitService(applicationContext));
  }
}

class MyProvider implements DbMapperProvider {

  @Override
  public <T> Optional<DbMapper<T>> mapper(Class<T> type) {
    return Optional.empty();
  }
}


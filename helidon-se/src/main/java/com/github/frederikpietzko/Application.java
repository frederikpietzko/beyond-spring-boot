package com.github.frederikpietzko;

import io.helidon.config.Config;
import io.helidon.logging.common.LogConfig;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.HttpRouting;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Application {
  private Application() {}

  public static void main(String[] args) {
    LogConfig.configureRuntime();

    final var global = Config.global();
    final var server = WebServer.builder()
      .config(global.get("server"))
      .routing(Application::routing)
      .build()
      .start();
    log.info("WEB Server started at http://localhost:{}", server.port());
  }

  static void routing(HttpRouting.Builder routing) {

  }
}

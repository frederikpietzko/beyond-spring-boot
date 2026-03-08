package com.github.frederikpietzko

import com.github.frederikpietzko.model.Type
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone

object PetTable : LongIdTable("pet") {
  val name = varchar("name", 255)
  val age = integer("age")
  val type = enumerationByName<Type>("type", 255)
}

object VisitTable : LongIdTable("visit") {
  val description = text("description")
  val pet = reference("pet_id", PetTable)
  val dateTime = timestampWithTimeZone("date_time")
}

object DbSettings {
  fun init(jdbcUrl: String, username: String, password: String, registry: PrometheusMeterRegistry) {
    val config = HikariConfig().apply {
      this.jdbcUrl = jdbcUrl
      this.username = username
      this.password = password
      driverClassName = "org.postgresql.Driver"
      minimumIdle = System.getenv("HIKARI_MIN_IDLE")?.toInt() ?: 10
      maximumPoolSize = System.getenv("HIKARI_MAX_POOL_SIZE")?.toInt() ?: 100
      metricRegistry = registry
    }
    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)
  }
}

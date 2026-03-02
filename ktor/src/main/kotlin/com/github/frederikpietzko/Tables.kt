package com.github.frederikpietzko

import com.github.frederikpietzko.model.Type
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

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

suspend fun initTables() = newSuspendedTransaction {
  SchemaUtils.drop(PetTable, VisitTable, inBatch = true)
  SchemaUtils.create(PetTable, VisitTable, inBatch = true)
  commit()
}

object DbSettings {
  fun init(jdbcUrl: String, username: String, password: String) {
    val config = HikariConfig().apply {
      this.jdbcUrl = jdbcUrl
      this.username = username
      this.password = password
      driverClassName = "org.postgresql.Driver"
      maximumPoolSize = 10
    }
    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)
  }
}

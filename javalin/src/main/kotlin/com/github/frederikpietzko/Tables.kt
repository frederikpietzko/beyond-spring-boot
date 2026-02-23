package com.github.frederikpietzko

import com.github.frederikpietzko.model.Type
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.javatime.timestampWithTimeZone
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.migration.MigrationUtils

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

private fun initTables() = transaction {
  val migration = MigrationUtils.statementsRequiredForDatabaseMigration(
    PetTable,
    VisitTable,
  )
  migration.forEach { exec(it) }
}


fun setupDatabase() {
  val config = HikariConfig().apply {
    jdbcUrl = "jdbc:postgresql://localhost:6432/postgres"
    username = "pg"
    password = "pg"
  }
  val dataSource = HikariDataSource(config)
  Database.connect(dataSource)
  initTables()
}

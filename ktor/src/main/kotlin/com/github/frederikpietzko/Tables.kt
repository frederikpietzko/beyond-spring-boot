package com.github.frederikpietzko

import com.github.frederikpietzko.model.Type
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.javatime.timestampWithTimeZone
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.SchemaUtils
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction

object PetTable : LongIdTable("pet") {
  val name = varchar("name", 255)
  val age = integer("age")
  val type = enumeration<Type>("type")
}

object VisitTable : LongIdTable("visit") {
  val description = text("description")
  val pet = reference("pet_id", PetTable)
  val dateTime = timestampWithTimeZone("date_time")
}

suspend fun initTables() = suspendTransaction(db = DbSettings.db) {
  SchemaUtils.drop(PetTable, VisitTable, inBatch = true)
  SchemaUtils.create(PetTable, VisitTable, inBatch = true)
  commit()
}

object DbSettings {
  val db by lazy {
    R2dbcDatabase.connect(
      url = "r2dbc:postgresql://localhost:5432/postgres",
      user = "pg",
      password = "pg",
    )
  }
}

package com.github.frederikpietzko

import com.github.frederikpietzko.model.Pet
import com.github.frederikpietzko.model.Visit
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object VisitRepository {
  fun getVisits() = transaction {
    VisitTable
      .join(PetTable, JoinType.INNER)
      .selectAll()
      .map { it.toVisit() }
  }

  fun findVisitById(id: Long) = transaction {
    VisitTable
      .join(PetTable, JoinType.INNER)
      .selectAll()
      .where { VisitTable.id eq id }
      .map { it.toVisit() }
      .singleOrNull()
  }

  fun save(visit: Visit) = transaction {
    val petId = visit.pet.id ?: PetTable.insertAndGetId { row ->
      row[name] = visit.pet.name
      row[age] = visit.pet.age
      row[type] = visit.pet.type
    }.value

    val visitId = VisitTable.insertAndGetId { row ->
      row[description] = visit.description
      row[pet] = petId
      row[dateTime] = visit.dateTime
    }.value

    visit.copy(id = visitId, pet = visit.pet.copy(id = petId))
  }

  private fun ResultRow.toVisit() = Visit(
    id = this[VisitTable.id].value,
    description = this[VisitTable.description],
    pet = Pet(
      id = this[PetTable.id].value,
      name = this[PetTable.name],
      age = this[PetTable.age],
      type = this[PetTable.type],
    ),
    dateTime = this[VisitTable.dateTime],
  )
}

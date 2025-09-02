package com.github.frederikpietzko

import com.github.frederikpietzko.model.Pet
import com.github.frederikpietzko.model.Visit
import org.jetbrains.exposed.v1.core.JoinType
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

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
      .where { PetTable.id eq id }
      .map { it.toVisit() }
      .singleOrNull()
  }

  fun save(visit: Visit) = transaction {
    val petId = visit.pet.id ?: PetTable.insertAndGetId {
      it[name] = visit.pet.name
      it[age] = visit.pet.age
      it[type] = visit.pet.type
    }.value

    val visitId = VisitTable.insertAndGetId {
      it[description] = visit.description
      it[pet] = petId
      it[dateTime] = visit.dateTime
    }.value

    visit.copy(id = visitId, pet = visit.pet.copy(id = petId))
  }

  private fun ResultRow.toVisit() = Visit(
    id = this[VisitTable.id].value,
    description = this[VisitTable.description],
    pet = Pet(
      id = this[VisitTable.id].value,
      name = this[PetTable.name],
      age = this[PetTable.age],
      type = this[PetTable.type],
    ),
    dateTime = this[VisitTable.dateTime],
  )
}

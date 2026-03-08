package com.github.frederikpietzko

import com.github.frederikpietzko.model.Pet
import com.github.frederikpietzko.model.Visit
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object VisitRepository {
  suspend fun getVisits() = newSuspendedTransaction {
    VisitTable
      .join(PetTable, JoinType.INNER)
      .selectAll()
      .map { it.toVisit() }
  }

  suspend fun findVisitById(id: Long) = newSuspendedTransaction {
    VisitTable
      .join(PetTable, JoinType.INNER)
      .selectAll()
      .where { VisitTable.id eq id }
      .map { it.toVisit() }
      .singleOrNull()
  }

  suspend fun save(visit: Visit) = newSuspendedTransaction {
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
      id = this[PetTable.id].value,
      name = this[PetTable.name],
      age = this[PetTable.age],
      type = this[PetTable.type],
    ),
    dateTime = this[VisitTable.dateTime],
  )
}

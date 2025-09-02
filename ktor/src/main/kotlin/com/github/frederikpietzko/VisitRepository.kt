package com.github.frederikpietzko

import com.github.frederikpietzko.model.Pet
import com.github.frederikpietzko.model.Visit
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.v1.core.JoinType
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.r2dbc.insertAndGetId
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction

object VisitRepository {
  suspend fun getVisits() = suspendTransaction(db = DbSettings.db) {
    VisitTable
      .join(PetTable, JoinType.INNER)
      .selectAll()
      .map { it.toVisit() }
      .toList()
  }

  suspend fun findVisitById(id: Long) = suspendTransaction {
    VisitTable
      .join(PetTable, JoinType.INNER)
      .selectAll()
      .where { PetTable.id eq id }
      .map { it.toVisit() }
      .singleOrNull()
  }

  suspend fun save(visit: Visit) = suspendTransaction {
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

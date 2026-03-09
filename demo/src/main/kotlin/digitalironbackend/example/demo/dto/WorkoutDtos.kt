package digitalironbackend.example.demo.dto

import digitalironbackend.example.demo.model.SetType
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class WorkoutRequest(
    val splitDayId: UUID? = null,
    val date: LocalDate = LocalDate.now()
)

data class WorkoutResponse(
    val id: UUID,
    val userId: UUID,
    val splitDayId: UUID?,
    val date: LocalDate,
    val exercises: List<WorkoutExerciseResponse> = emptyList()
)

data class WorkoutExerciseResponse(
    val id: UUID,
    val exerciseId: UUID,
    val exerciseName: String,
    val orderIndex: Int,
    val sets: List<WorkoutSetResponse> = emptyList()
)

data class WorkoutSetRequest(
    val setNumber: Int,
    val reps: Int? = null,
    val weight: BigDecimal? = null,
    val setType: SetType = SetType.NORMAL
)

data class WorkoutSetResponse(
    val id: UUID,
    val setNumber: Int,
    val reps: Int?,
    val weight: BigDecimal?,
    val setType: SetType
)
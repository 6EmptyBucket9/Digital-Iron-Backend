package digitalironbackend.example.demo.dto

import java.math.BigDecimal
import java.util.UUID

data class WorkoutSplitRequest(
    val name: String
)

data class WorkoutSplitResponse(
    val id: UUID,
    val name: String,
    val splitDays: List<SplitDayResponse> = emptyList()
)

data class SplitDayRequest(
    val dayNumber: Int
)

data class SplitDayResponse(
    val id: UUID,
    val dayNumber: Int,
    val exercises: List<SplitExerciseResponse> = emptyList()
)

data class SplitExerciseRequest(
    val exerciseId: UUID,
    val targetSets: Int? = null,
    val targetReps: Int? = null,
    val targetWeight: BigDecimal? = null,
    val orderIndex: Int = 0
)

data class SplitExerciseResponse(
    val id: UUID,
    val exerciseId: UUID,
    val exerciseName: String,
    val targetSets: Int?,
    val targetReps: Int?,
    val targetWeight: BigDecimal?,
    val orderIndex: Int
)
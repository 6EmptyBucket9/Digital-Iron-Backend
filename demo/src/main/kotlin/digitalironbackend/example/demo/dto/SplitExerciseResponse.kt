package digitalironbackend.example.demo.dto

data class SplitExerciseResponse(
val id: Long,
val splitId: Long,
val exerciseId: Long,
val targetSets: Int,
val targetReps: Int,
val targetWeight: Double,
val orderIndex: Int
)
data class CreateSplitExerciseRequest(
    val splitId: Long,
    val exerciseId: Long,
    val targetSets: Int,
    val targetReps: Int,
    val targetWeight: Double,
    val orderIndex: Int
)
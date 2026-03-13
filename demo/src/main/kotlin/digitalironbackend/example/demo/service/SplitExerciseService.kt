package digitalironbackend.example.demo.service

import digitalironbackend.example.demo.dto.CreateSplitExerciseRequest
import digitalironbackend.example.demo.dto.SplitExerciseResponse
import digitalironbackend.example.demo.model.SplitExercise
import digitalironbackend.example.demo.repository.ExerciseRepository
import digitalironbackend.example.demo.repository.SplitExerciseRepository
import digitalironbackend.example.demo.repository.TrainingSplitRepository
import org.springframework.stereotype.Service

@Service
class SplitExerciseService(
    private val splitExerciseRepository: SplitExerciseRepository,
    private val splitRepository: TrainingSplitRepository,
    private val exerciseRepository: ExerciseRepository
) {

    fun findBySplitUserId(userId: Long): List<SplitExerciseResponse> {
        return splitExerciseRepository.findBySplitUserId(userId)
            .map {
                SplitExerciseResponse(
                    id = it.id,
                    splitId = it.split.id,
                    exerciseId = it.exercise.id,
                    targetSets = it.target_sets,
                    targetReps = it.target_reps,
                    targetWeight = it.target_weight,
                    orderIndex = it.order_index
                )
            }
    }

    fun addSplitExercise(userId: Long, request: CreateSplitExerciseRequest): SplitExerciseResponse {
        // Find split
        val split = splitRepository.findByIdAndUserId(request.splitId, userId)
            ?: throw IllegalArgumentException("Split not found or does not belong to user")

        // Find exercise
        val exercise = exerciseRepository.findById(request.exerciseId)
            .orElseThrow { IllegalArgumentException("Exercise not found") }

        // Create new SplitExercise
        val splitExercise = SplitExercise(
            split = split,
            exercise = exercise,
            target_sets = request.targetSets,
            target_reps = request.targetReps,
            target_weight = request.targetWeight,
            order_index = request.orderIndex
        )

        val saved = splitExerciseRepository.save(splitExercise)

        // Map to DTO
        return SplitExerciseResponse(
            id = saved.id,
            splitId = saved.split.id,
            exerciseId = saved.exercise.id,
            targetSets = saved.target_sets,
            targetReps = saved.target_reps,
            targetWeight = saved.target_weight,
            orderIndex = saved.order_index
        )
    }
}
package digitalironbackend.example.demo.service

import digitalironbackend.example.demo.dto.*
import digitalironbackend.example.demo.model.SplitDay
import digitalironbackend.example.demo.model.SplitExercise
import digitalironbackend.example.demo.model.WorkoutSplit
import digitalironbackend.example.demo.repository.*
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class WorkoutSplitService(
    private val splitRepository: WorkoutSplitRepository,
    private val splitDayRepository: SplitDayRepository,
    private val splitExerciseRepository: SplitExerciseRepository,
    private val userRepository: UserRepository,
    private val exerciseRepository: ExerciseRepository
) {

    fun getAllForUser(userId: UUID): List<WorkoutSplitResponse> =
        splitRepository.findAllByUserId(userId).map { it.toResponse(includeDays = false) }

    fun getById(id: UUID): WorkoutSplitResponse {
        val split = splitRepository.findById(id)
            .orElseThrow { NoSuchElementException("Split niet gevonden") }
        return split.toResponse(includeDays = true)
    }

    fun create(userId: UUID, request: WorkoutSplitRequest): WorkoutSplitResponse {
        val user = userRepository.findById(userId)
            .orElseThrow { NoSuchElementException("Gebruiker niet gevonden") }
        val split = WorkoutSplit(user = user, name = request.name)
        return splitRepository.save(split).toResponse(includeDays = false)
    }

    fun update(id: UUID, request: WorkoutSplitRequest): WorkoutSplitResponse {
        val split = splitRepository.findById(id)
            .orElseThrow { NoSuchElementException("Split niet gevonden") }
        return splitRepository.save(split.copy(name = request.name)).toResponse(includeDays = false)
    }

    fun delete(id: UUID) {
        if (!splitRepository.existsById(id)) throw NoSuchElementException("Split niet gevonden")
        splitRepository.deleteById(id)
    }

    // --- Split Days ---

    fun addDay(splitId: UUID, request: SplitDayRequest): SplitDayResponse {
        val split = splitRepository.findById(splitId)
            .orElseThrow { NoSuchElementException("Split niet gevonden") }
        val day = SplitDay(workoutSplit = split, dayNumber = request.dayNumber)
        return splitDayRepository.save(day).toResponse(includeExercises = false)
    }

    fun updateDay(splitId: UUID, dayId: UUID, request: SplitDayRequest): SplitDayResponse {
        val day = splitDayRepository.findById(dayId)
            .orElseThrow { NoSuchElementException("Dag niet gevonden") }
        return splitDayRepository.save(day.copy(dayNumber = request.dayNumber))
            .toResponse(includeExercises = false)
    }

    fun deleteDay(splitId: UUID, dayId: UUID) {
        if (!splitDayRepository.existsById(dayId)) throw NoSuchElementException("Dag niet gevonden")
        splitDayRepository.deleteById(dayId)
    }

    // --- Split Exercises ---

    fun addExerciseToDay(splitId: UUID, dayId: UUID, request: SplitExerciseRequest): SplitExerciseResponse {
        val day = splitDayRepository.findById(dayId)
            .orElseThrow { NoSuchElementException("Dag niet gevonden") }
        val exercise = exerciseRepository.findById(request.exerciseId)
            .orElseThrow { NoSuchElementException("Oefening niet gevonden") }

        val splitExercise = SplitExercise(
            splitDay = day,
            exercise = exercise,
            targetSets = request.targetSets,
            targetReps = request.targetReps,
            targetWeight = request.targetWeight,
            orderIndex = request.orderIndex
        )
        return splitExerciseRepository.save(splitExercise).toResponse()
    }

    fun updateExerciseInDay(splitId: UUID, dayId: UUID, exerciseId: UUID, request: SplitExerciseRequest): SplitExerciseResponse {
        val splitExercise = splitExerciseRepository.findById(exerciseId)
            .orElseThrow { NoSuchElementException("Oefening in split niet gevonden") }

        val updated = splitExercise.copy(
            targetSets = request.targetSets,
            targetReps = request.targetReps,
            targetWeight = request.targetWeight,
            orderIndex = request.orderIndex
        )
        return splitExerciseRepository.save(updated).toResponse()
    }

    fun removeExerciseFromDay(splitId: UUID, dayId: UUID, exerciseId: UUID) {
        if (!splitExerciseRepository.existsById(exerciseId)) throw NoSuchElementException("Oefening in split niet gevonden")
        splitExerciseRepository.deleteById(exerciseId)
    }

    // --- Mappers ---

    private fun WorkoutSplit.toResponse(includeDays: Boolean) = WorkoutSplitResponse(
        id = id,
        name = name,
        splitDays = if (includeDays) splitDays.map { it.toResponse(includeExercises = true) } else emptyList()
    )

    private fun SplitDay.toResponse(includeExercises: Boolean) = SplitDayResponse(
        id = id,
        dayNumber = dayNumber,
        exercises = if (includeExercises) splitExercises.map { it.toResponse() } else emptyList()
    )

    private fun SplitExercise.toResponse() = SplitExerciseResponse(
        id = id,
        exerciseId = exercise.id,
        exerciseName = exercise.name,
        targetSets = targetSets,
        targetReps = targetReps,
        targetWeight = targetWeight,
        orderIndex = orderIndex
    )
}
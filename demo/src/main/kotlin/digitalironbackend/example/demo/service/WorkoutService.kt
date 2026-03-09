package digitalironbackend.example.demo.service

import digitalironbackend.example.demo.dto.*
import digitalironbackend.example.demo.model.Workout
import digitalironbackend.example.demo.model.WorkoutExercise
import digitalironbackend.example.demo.model.WorkoutSet
import digitalironbackend.example.demo.repository.*
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class WorkoutService(
    private val workoutRepository: WorkoutRepository,
    private val workoutExerciseRepository: WorkoutExerciseRepository,
    private val workoutSetRepository: WorkoutSetRepository,
    private val userRepository: UserRepository,
    private val exerciseRepository: ExerciseRepository,
    private val splitDayRepository: SplitDayRepository
) {

    fun getAllForUser(userId: UUID): List<WorkoutResponse> =
        workoutRepository.findAllByUserIdOrderByDateDesc(userId).map { it.toResponse(includeExercises = false) }

    fun getById(id: UUID): WorkoutResponse {
        val workout = workoutRepository.findById(id)
            .orElseThrow { NoSuchElementException("Workout niet gevonden") }
        return workout.toResponse(includeExercises = true)
    }

    fun create(userId: UUID, request: WorkoutRequest): WorkoutResponse {
        val user = userRepository.findById(userId)
            .orElseThrow { NoSuchElementException("Gebruiker niet gevonden") }

        val splitDay = request.splitDayId?.let {
            splitDayRepository.findById(it).orElseThrow { NoSuchElementException("Split dag niet gevonden") }
        }

        val workout = Workout(user = user, splitDay = splitDay, date = request.date)
        return workoutRepository.save(workout).toResponse(includeExercises = false)
    }

    fun update(id: UUID, request: WorkoutRequest): WorkoutResponse {
        val workout = workoutRepository.findById(id)
            .orElseThrow { NoSuchElementException("Workout niet gevonden") }

        val splitDay = request.splitDayId?.let {
            splitDayRepository.findById(it).orElseThrow { NoSuchElementException("Split dag niet gevonden") }
        }

        return workoutRepository.save(workout.copy(splitDay = splitDay, date = request.date))
            .toResponse(includeExercises = false)
    }

    fun delete(id: UUID) {
        if (!workoutRepository.existsById(id)) throw NoSuchElementException("Workout niet gevonden")
        workoutRepository.deleteById(id)
    }

    // --- Workout Sets ---

    fun addSet(workoutId: UUID, exerciseId: UUID, request: WorkoutSetRequest): WorkoutSetResponse {
        val workoutExercise = workoutExerciseRepository.findAllByWorkoutId(workoutId)
            .firstOrNull { it.exercise.id == exerciseId }
            ?: run {
                val workout = workoutRepository.findById(workoutId)
                    .orElseThrow { NoSuchElementException("Workout niet gevonden") }
                val exercise = exerciseRepository.findById(exerciseId)
                    .orElseThrow { NoSuchElementException("Oefening niet gevonden") }
                val newWE = WorkoutExercise(workout = workout, exercise = exercise, orderIndex = 0)
                workoutExerciseRepository.save(newWE)
            }

        val set = WorkoutSet(
            workoutExercise = workoutExercise,
            setNumber = request.setNumber,
            reps = request.reps,
            weight = request.weight,
            setType = request.setType
        )
        return workoutSetRepository.save(set).toResponse()
    }

    fun updateSet(workoutId: UUID, exerciseId: UUID, setId: UUID, request: WorkoutSetRequest): WorkoutSetResponse {
        val set = workoutSetRepository.findById(setId)
            .orElseThrow { NoSuchElementException("Set niet gevonden") }

        return workoutSetRepository.save(
            set.copy(reps = request.reps, weight = request.weight, setType = request.setType)
        ).toResponse()
    }

    fun deleteSet(workoutId: UUID, exerciseId: UUID, setId: UUID) {
        if (!workoutSetRepository.existsById(setId)) throw NoSuchElementException("Set niet gevonden")
        workoutSetRepository.deleteById(setId)
    }

    // --- Mappers ---

private fun Workout.toResponse(includeExercises: Boolean) = WorkoutResponse(
    id = this.id,
    userId = this.user.id,
    splitDayId = this.splitDay?.id,
    date = date,
    exercises = if (includeExercises) workoutExercises.map { it.toResponse() } else emptyList()
)

private fun WorkoutExercise.toResponse() = WorkoutExerciseResponse(
    id = this.id,
    exerciseId = this.exercise.id,
    exerciseName = exercise.name,
    orderIndex = orderIndex,
    sets = sets.map { it.toResponse() }
)

private fun WorkoutSet.toResponse() = WorkoutSetResponse(
    id = this.id,
    setNumber = setNumber,
    reps = reps,
    weight = weight,
    setType = setType
)

}
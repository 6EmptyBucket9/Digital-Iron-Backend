package digitalironbackend.example.demo.service

import digitalironbackend.example.demo.dto.PersonalRecordRequest
import digitalironbackend.example.demo.dto.PersonalRecordResponse
import digitalironbackend.example.demo.model.PersonalRecord
import digitalironbackend.example.demo.repository.ExerciseRepository
import digitalironbackend.example.demo.repository.PersonalRecordRepository
import digitalironbackend.example.demo.repository.UserRepository
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class PersonalRecordService(
    private val prRepository: PersonalRecordRepository,
    private val userRepository: UserRepository,
    private val exerciseRepository: ExerciseRepository
) {

    fun getAllForUser(userId: UUID): List<PersonalRecordResponse> =
        prRepository.findAllByUserId(userId).map { it.toResponse() }

    fun getByExercise(userId: UUID, exerciseId: UUID): PersonalRecordResponse =
        prRepository.findByUserIdAndExerciseId(userId, exerciseId)?.toResponse()
            ?: throw NoSuchElementException("PR niet gevonden voor deze oefening")

    fun create(userId: UUID, request: PersonalRecordRequest): PersonalRecordResponse {
        val user = userRepository.findById(userId)
            .orElseThrow { NoSuchElementException("Gebruiker niet gevonden") }
        val exercise = exerciseRepository.findById(request.exerciseId)
            .orElseThrow { NoSuchElementException("Oefening niet gevonden") }

        val pr = PersonalRecord(
            user = user,
            exercise = exercise,
            weight = request.weight,
            reps = request.reps,
            estimated1rm = request.estimated1rm,
            date = request.date
        )
        return prRepository.save(pr).toResponse()
    }

    fun update(userId: UUID, id: UUID, request: PersonalRecordRequest): PersonalRecordResponse {
        val pr = prRepository.findById(id)
            .orElseThrow { NoSuchElementException("PR niet gevonden") }

        return prRepository.save(
            pr.copy(
                weight = request.weight,
                reps = request.reps,
                estimated1rm = request.estimated1rm,
                date = request.date
            )
        ).toResponse()
    }

    fun delete(id: UUID) {
        if (!prRepository.existsById(id)) throw NoSuchElementException("PR niet gevonden")
        prRepository.deleteById(id)
    }

    private fun PersonalRecord.toResponse() = PersonalRecordResponse(
        id = id,
        exerciseId = exercise.id,
        exerciseName = exercise.name,
        weight = weight,
        reps = reps,
        estimated1rm = estimated1rm,
        date = date
    )
}
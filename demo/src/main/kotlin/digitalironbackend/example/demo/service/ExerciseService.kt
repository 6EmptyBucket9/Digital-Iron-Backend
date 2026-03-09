package digitalironbackend.example.demo.service

import digitalironbackend.example.demo.dto.ExerciseRequest
import digitalironbackend.example.demo.dto.ExerciseResponse
import digitalironbackend.example.demo.model.Exercise
import digitalironbackend.example.demo.repository.ExerciseRepository
import org.springframework.stereotype.Service

@Service
class ExerciseService(private val exerciseRepository: ExerciseRepository) {

    fun getAll(): List<ExerciseResponse> =
        exerciseRepository.findAll().map { it.toResponse() }

    fun getById(id: String): ExerciseResponse =
        exerciseRepository.findById(id)
            .orElseThrow { NoSuchElementException("Oefening niet gevonden") }
            .toResponse()

    fun create(request: ExerciseRequest): ExerciseResponse {
        val exercise = Exercise(
            name = request.name,
            description = request.description,
            createdByUser = request.createdByUser
        )
        return exerciseRepository.save(exercise).toResponse()
    }

    fun update(id: String, request: ExerciseRequest): ExerciseResponse {
        val exercise = exerciseRepository.findById(id)
            .orElseThrow { NoSuchElementException("Oefening niet gevonden") }

        val updated = exercise.copy(
            name = request.name,
            description = request.description
        )
        return exerciseRepository.save(updated).toResponse()
    }

    fun delete(id: String) {
        if (!exerciseRepository.existsById(id)) throw NoSuchElementException("Oefening niet gevonden")
        exerciseRepository.deleteById(id)
    }

    private fun Exercise.toResponse() = ExerciseResponse(
        id = id,
        name = name,
        description = description,
        createdByUser = createdByUser
    )
}
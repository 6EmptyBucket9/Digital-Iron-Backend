package digitalironbackend.example.demo.service

import digitalironbackend.example.demo.model.Exercise
import digitalironbackend.example.demo.repository.ExerciseRepository
import org.springframework.stereotype.Service

@Service
class ExerciseService(private val exerciseRepository: ExerciseRepository) {

    fun getAllExercises(): List<Exercise> =
        exerciseRepository.findAll()
}
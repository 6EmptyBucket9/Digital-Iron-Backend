package digitalironbackend.example.demo.repository

import digitalironbackend.example.demo.model.TrainingDay
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import digitalironbackend.example.demo.model.Exercise
@Repository
interface ExerciseRepository : JpaRepository<Exercise, Long> {
    
}
package digitalironbackend.example.demo.repository

import digitalironbackend.example.demo.model.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ExerciseRepository : JpaRepository<Exercise, UUID> {
    fun findAllByCreatedByUserFalse(): List<Exercise>
    fun findAllByCreatedByUserTrue(): List<Exercise>
}

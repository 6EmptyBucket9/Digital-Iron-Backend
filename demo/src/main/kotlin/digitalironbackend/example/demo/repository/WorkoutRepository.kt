package digitalironbackend.example.demo.repository

import digitalironbackend.example.demo.model.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface WorkoutRepository : JpaRepository<Workout, UUID> {
    fun findAllByUserId(userId: UUID): List<Workout>
    fun findAllByUserIdOrderByDateDesc(userId: UUID): List<Workout>
}

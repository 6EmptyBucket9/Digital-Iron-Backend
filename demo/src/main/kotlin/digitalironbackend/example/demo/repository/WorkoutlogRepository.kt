package digitalironbackend.example.demo.repository

import digitalironbackend.example.demo.model.WorkoutLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface WorkoutLogRepository : JpaRepository<WorkoutLog, Long> {

    // Most recent workout for a user
    fun findTopByUserIdOrderByDateDesc(userId: Long): Optional<WorkoutLog>

    // All workouts for a user ordered by date descending
    fun findByUserIdOrderByDateDesc(userId: Long): List<WorkoutLog>

    // Count workouts for a user
    fun countByUserId(userId: Long): Long
}
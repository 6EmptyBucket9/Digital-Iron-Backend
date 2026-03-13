package digitalironbackend.example.demo.repository

import digitalironbackend.example.demo.model.SplitExercise
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SplitExerciseRepository : JpaRepository<SplitExercise, Long> {
    fun findBySplitUserId(userId: Long): List<SplitExercise>
}
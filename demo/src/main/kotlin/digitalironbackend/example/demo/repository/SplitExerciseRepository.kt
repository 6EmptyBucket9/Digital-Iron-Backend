package digitalironbackend.example.demo.repository

import digitalironbackend.example.demo.model.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SplitExerciseRepository : JpaRepository<SplitExercise, UUID> {
    fun findAllBySplitDayId(splitDayId: UUID): List<SplitExercise>
}

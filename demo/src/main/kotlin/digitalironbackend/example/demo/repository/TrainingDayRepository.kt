package digitalironbackend.example.demo.repository

import digitalironbackend.example.demo.model.TrainingDay
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TrainingDayRepository : JpaRepository<TrainingDay, Long> {
    fun findBySplitIdOrderByDayNumberAsc(splitId: Long): List<TrainingDay>
}
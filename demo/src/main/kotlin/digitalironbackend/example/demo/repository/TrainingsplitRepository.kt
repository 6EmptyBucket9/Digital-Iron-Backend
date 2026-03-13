package digitalironbackend.example.demo.repository

import digitalironbackend.example.demo.model.TrainingSplit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface TrainingSplitRepository : JpaRepository<TrainingSplit, Long> {

    fun findFirstByUserId(userId: Long): Optional<TrainingSplit>

    fun findAllByUserId(userId: Long): List<TrainingSplit>

    fun existsByUserId(userId: Long): Boolean
        fun findByIdAndUserId(id: Long, userId: Long): TrainingSplit?
}
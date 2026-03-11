package digitalironbackend.example.demo.repository

import digitalironbackend.example.demo.model.TrainingSplit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface TrainingSplitRepository : JpaRepository<TrainingSplit, Long> {

    // Get the user's (first) active split
    fun findFirstByUserId(userId: Long): Optional<TrainingSplit>

    // Check if user has any split
    fun existsByUserId(userId: Long): Boolean
}
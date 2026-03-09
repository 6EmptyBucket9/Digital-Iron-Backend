package digitalironbackend.example.demo.repository

import digitalironbackend.example.demo.model.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PersonalRecordRepository : JpaRepository<PersonalRecord, UUID> {
    fun findAllByUserId(userId: UUID): List<PersonalRecord>
    fun findByUserIdAndExerciseId(userId: UUID, exerciseId: UUID): PersonalRecord?
}

package digitalironbackend.example.demo.dto

import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class PersonalRecordRequest(
    val exerciseId: UUID,
    val weight: BigDecimal? = null,
    val reps: Int? = null,
    val estimated1rm: BigDecimal? = null,
    val date: LocalDate = LocalDate.now()
)

data class PersonalRecordResponse(
    val id: UUID,
    val exerciseId: UUID,
    val exerciseName: String,
    val weight: BigDecimal?,
    val reps: Int?,
    val estimated1rm: BigDecimal?,
    val date: LocalDate
)
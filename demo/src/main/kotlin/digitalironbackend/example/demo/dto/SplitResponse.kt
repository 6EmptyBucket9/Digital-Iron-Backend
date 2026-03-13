package digitalironbackend.example.demo.dto

data class TrainingDayResponse(
    val id: Long,
    val dayNumber: Int,
    val name: String
)

data class SplitResponse(
    val id: Long,
    val name: String,
    val days: List<TrainingDayResponse>
)
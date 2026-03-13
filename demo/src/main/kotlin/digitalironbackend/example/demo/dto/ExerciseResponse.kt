package digitalironbackend.example.demo.dto

data class ExerciseResponse(
    val id: Long? = null,
    val name: String,
    val description: String? = null,
    val createdByUserId: Long? = null
)
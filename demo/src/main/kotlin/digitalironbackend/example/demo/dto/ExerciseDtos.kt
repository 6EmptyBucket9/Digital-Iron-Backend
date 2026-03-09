package digitalironbackend.example.demo.dto

data class ExerciseRequest(
    val name: String,
    val description: String? = null,
    val createdByUser: Boolean = false
)

data class ExerciseResponse(
    val id: String,
    val name: String,
    val description: String?,
    val createdByUser: Boolean
)

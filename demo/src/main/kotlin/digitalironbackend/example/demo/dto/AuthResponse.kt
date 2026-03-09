package digitalironbackend.example.demo.dto
data class AuthResponse(
    val token: String,
    val userId: String,
    val username: String
)
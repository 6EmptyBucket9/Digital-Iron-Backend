package digitalironbackend.example.demo.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

// ── Register ──────────────────────────────────────────────────────────────────

data class RegisterRequest(

    @field:NotBlank(message = "Name is required")
    @field:Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    val name: String,

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email must be a valid address")
    val email: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 8, message = "Password must be at least 8 characters")
    val password: String
)

// ── Login ─────────────────────────────────────────────────────────────────────

data class LoginRequest(

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email must be a valid address")
    val email: String,

    @field:NotBlank(message = "Password is required")
    val password: String
)

// ── Auth Response ─────────────────────────────────────────────────────────────

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer",
    val user: UserResponse
)

// ── User Response (safe – no password) ───────────────────────────────────────

data class UserResponse(
    val id: Long,
    val name: String,
    val email: String,
    val role: String
)

// ── Error Response ────────────────────────────────────────────────────────────

data class ErrorResponse(
    val status: Int,
    val error: String,
    val message: String,
    val path: String
)
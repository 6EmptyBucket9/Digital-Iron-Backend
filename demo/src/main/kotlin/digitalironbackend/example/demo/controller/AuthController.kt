package digitalironbackend.example.demo.controller

import digitalironbackend.example.demo.dto.AuthResponse
import digitalironbackend.example.demo.dto.LoginRequest
import digitalironbackend.example.demo.dto.RegisterRequest
import digitalironbackend.example.demo.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val authService: AuthService) {

    /**
     * POST /api/v1/auth/register
     * Creates a new user account and returns JWT tokens.
     *
     * Request body:
     *   { "username": "john", "email": "john@example.com", "password": "secret123" }
     *
     * Responses:
     *   201 Created  – account created, returns AuthResponse with tokens
     *   400 Bad Request – validation errors
     *   409 Conflict – email or username already taken
     */
    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<AuthResponse> {
        val response = authService.register(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    /**
     * POST /api/v1/auth/login
     * Authenticates an existing user and returns fresh JWT tokens.
     *
     * Request body:
     *   { "email": "john@example.com", "password": "secret123" }
     *
     * Responses:
     *   200 OK        – authentication successful, returns AuthResponse
     *   400 Bad Request – validation errors
     *   401 Unauthorized – wrong credentials
     */
    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<AuthResponse> {
        val response = authService.login(request)
        return ResponseEntity.ok(response)
    }

    /**
     * POST /api/v1/auth/logout
     * Client-side logout — instructs the client to discard its JWT.
     * Token invalidation is handled on the client (stateless API).
     *
     * Responses:
     *   204 No Content
     */
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(): ResponseEntity<Void> = ResponseEntity.noContent().build()
}
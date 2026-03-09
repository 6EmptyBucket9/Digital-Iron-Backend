package digitalironbackend.example.demo.controller


import digitalironbackend.example.demo.dto.AuthResponse
import digitalironbackend.example.demo.dto.LoginRequest
import digitalironbackend.example.demo.dto.RegisterRequest
import digitalironbackend.example.demo.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<AuthResponse> {
        val response = authService.register(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<AuthResponse> {
        val response = authService.login(request)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/logout")
    fun logout(): ResponseEntity<Map<String, String>> {
        // JWT is stateless; de client verwijdert het token zelf.
        // Optioneel: implementeer een token blacklist via Redis.
        return ResponseEntity.ok(mapOf("message" to "Succesvol uitgelogd"))
    }
}
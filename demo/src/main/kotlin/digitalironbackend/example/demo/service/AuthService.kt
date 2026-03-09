package digitalironbackend.example.demo.service

import digitalironbackend.example.demo.dto.AuthResponse
import digitalironbackend.example.demo.dto.LoginRequest
import digitalironbackend.example.demo.dto.RegisterRequest
import digitalironbackend.example.demo.model.User
import digitalironbackend.example.demo.repository.UserRepository
import digitalironbackend.example.demo.security.JwtService
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {

    fun register(request: RegisterRequest): AuthResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email is already in use")
        }

        if (userRepository.existsByUsername(request.username)) {
            throw IllegalArgumentException("Username is already in use")
        }

        val user = User(
            email = request.email,
            username = request.username,
            passwordHash = passwordEncoder.encode(request.password)
        )

        val savedUser = userRepository.save(user)

      val userId = requireNotNull(savedUser.id) { "User ID cannot be null" }.toString()
      val token = jwtService.generateToken(userId)


        return AuthResponse(
            token = token,
            userId = userId,
            username = savedUser.username
        )
    }

    fun login(request: LoginRequest): AuthResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw BadCredentialsException("Invalid login credentials")

        if (!passwordEncoder.matches(request.password, user.passwordHash)) {
            throw BadCredentialsException("Invalid login credentials")
        }

        val userId: String = user.id!!  // <--- force non-null
        val token = jwtService.generateToken(userId)

        return AuthResponse(
            token = token,
            userId = userId,
            username = user.username
        )
    }
}
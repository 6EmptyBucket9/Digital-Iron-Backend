package digitalironbackend.example.demo.service

import digitalironbackend.example.demo.dto.AuthResponse
import digitalironbackend.example.demo.dto.LoginRequest
import digitalironbackend.example.demo.dto.RegisterRequest
import digitalironbackend.example.demo.dto.UserResponse
import digitalironbackend.example.demo.model.User
import digitalironbackend.example.demo.exception.EmailAlreadyExistsException
import digitalironbackend.example.demo.exception.InvalidCredentialsException
import digitalironbackend.example.demo.exception.UsernameAlreadyExistsException
import digitalironbackend.example.demo.repository.UserRepository
import digitalironbackend.example.demo.service.JwtService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager
) {

    /**
     * Registers a new user account.
     * Validates email and username uniqueness, hashes the password,
     * persists the user and returns a JWT token pair.
     */
    @Transactional
    fun register(request: RegisterRequest): AuthResponse {
        // Check uniqueness before saving
        if (userRepository.existsByEmail(request.email)) {
            throw EmailAlreadyExistsException(request.email)
        }
        if (userRepository.existsByName(request.name)) {
            throw UsernameAlreadyExistsException(request.name)
        }

        val user = User(
            name = request.name,
            email    = request.email,
            password = passwordEncoder.encode(request.password)!!
        )

        val savedUser = userRepository.save(user)

        return AuthResponse(
            accessToken  = jwtService.generateAccessToken(savedUser),
            refreshToken = jwtService.generateRefreshToken(savedUser),
            user         = savedUser.toResponse()
        )
    }

    /**
     * Authenticates an existing user with email + password.
     * Returns a fresh JWT token pair on success.
     */
    fun login(request: LoginRequest): AuthResponse {
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.email, request.password)
            )
        } catch (ex: BadCredentialsException) {
            throw InvalidCredentialsException()
        }

        val user = userRepository.findByEmail(request.email)
            .orElseThrow { InvalidCredentialsException() }

        return AuthResponse(
            accessToken  = jwtService.generateAccessToken(user),
            refreshToken = jwtService.generateRefreshToken(user),
            user         = user.toResponse()
        )
    }

    // ── Mapping ───────────────────────────────────────────────────────────────

    private fun User.toResponse() = UserResponse(
        id       = id,
        name = name,
        email    = email,
        role     = role.name
    )
}
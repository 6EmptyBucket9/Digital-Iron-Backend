// package digitalironbackend.example.demo.service

// import digitalironbackend.example.demo.dto.LoginRequest
// import digitalironbackend.example.demo.dto.RegisterRequest
// import digitalironbackend.example.demo.exception.EmailAlreadyExistsException
// import digitalironbackend.example.demo.exception.InvalidCredentialsException
// import digitalironbackend.example.demo.exception.UsernameAlreadyExistsException
// import digitalironbackend.example.demo.model.Role
// import digitalironbackend.example.demo.model.User
// import digitalironbackend.example.demo.repository.UserRepository
// import io.mockk.*
// import org.junit.jupiter.api.Assertions.*
// import org.junit.jupiter.api.BeforeEach
// import org.junit.jupiter.api.Test
// import org.springframework.security.authentication.AuthenticationManager
// import org.springframework.security.authentication.BadCredentialsException
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
// import org.springframework.security.crypto.password.PasswordEncoder
// import java.util.*

// class AuthServiceTest {

//     private lateinit var userRepository: UserRepository
//     private lateinit var passwordEncoder: PasswordEncoder
//     private lateinit var jwtService: JwtService
//     private lateinit var authenticationManager: AuthenticationManager
//     private lateinit var authService: AuthService

//     @BeforeEach
//     fun setup() {
//         userRepository = mockk()
//         passwordEncoder = mockk()
//         jwtService = mockk()
//         authenticationManager = mockk()
//         authService = AuthService(userRepository, passwordEncoder, jwtService, authenticationManager)
//     }

//     // ── REGISTER ────────────────────────────────────────────────────────────────

//     @Test
//     fun `register should throw EmailAlreadyExistsException if email exists`() {
//         val request = RegisterRequest("user", "test@example.com", "password")
//         every { userRepository.existsByEmail(request.email) } returns true

//         assertThrows(EmailAlreadyExistsException::class.java) {
//             authService.register(request)
//         }
//         verify { userRepository.existsByEmail(request.email) }
//     }

//     @Test
//     fun `register should throw UsernameAlreadyExistsException if username exists`() {
//         val request = RegisterRequest("user", "test@example.com", "password")
//         every { userRepository.existsByEmail(request.email) } returns false
//         every { userRepository.existsByName(request.name) } returns true

//         assertThrows(UsernameAlreadyExistsException::class.java) {
//             authService.register(request)
//         }
//         verify { userRepository.existsByName(request.name) }
//     }

//     @Test
//     fun `register should save user and return AuthResponse`() {
//         val request = RegisterRequest("user", "test@example.com", "password")
//         val encodedPassword = "encodedPassword"
//         val savedUser = User(1L, "user", "test@example.com", encodedPassword, Role.USER)

//         every { userRepository.existsByEmail(request.email) } returns false
//         every { userRepository.existsByName(request.name) } returns false
//         every { passwordEncoder.encode(request.password) } returns encodedPassword
//         every { userRepository.save(any()) } returns savedUser
//         every { jwtService.generateAccessToken(savedUser) } returns "accessToken"
//         every { jwtService.generateRefreshToken(savedUser) } returns "refreshToken"

//         val response = authService.register(request)

//         assertEquals("accessToken", response.accessToken)
//         assertEquals("refreshToken", response.refreshToken)
//         assertEquals(savedUser.id, response.user.id)
//         assertEquals(savedUser.name, response.user.name)
//         assertEquals(savedUser.email, response.user.email)
//     }

//     // ── LOGIN ──────────────────────────────────────────────────────────────────

//     @Test
//     fun `login should throw InvalidCredentialsException if authentication fails`() {
//         val request = LoginRequest("test@example.com", "password")
//         every { authenticationManager.authenticate(any()) } throws BadCredentialsException("")

//         assertThrows(InvalidCredentialsException::class.java) {
//             authService.login(request)
//         }
//     }

//     @Test
//     fun `login should return AuthResponse on successful authentication`() {
//         val request = LoginRequest("test@example.com", "password")
//         val user = User(1L, "user", "test@example.com", "encodedPassword", Role.USER)

//         every { authenticationManager.authenticate(any()) } just runs
//         every { userRepository.findByEmail(request.email) } returns Optional.of(user)
//         every { jwtService.generateAccessToken(user) } returns "accessToken"
//         every { jwtService.generateRefreshToken(user) } returns "refreshToken"

//         val response = authService.login(request)

//         assertEquals("accessToken", response.accessToken)
//         assertEquals("refreshToken", response.refreshToken)
//         assertEquals(user.id, response.user.id)
//         assertEquals(user.name, response.user.name)
//         assertEquals(user.email, response.user.email)
//     }

//     @Test
//     fun `login should throw InvalidCredentialsException if user not found`() {
//         val request = LoginRequest("test@example.com", "password")

//         every { authenticationManager.authenticate(any()) } just runs
//         every { userRepository.findByEmail(request.email) } returns Optional.empty()

//         assertThrows(InvalidCredentialsException::class.java) {
//             authService.login(request)
//         }
//     }
// }
package digitalironbackend.example.demo.exception

import digitalironbackend.example.demo.dto.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException::class)
    fun handleEmailConflict(
        ex: EmailAlreadyExistsException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> = ResponseEntity.status(HttpStatus.CONFLICT).body(
        ErrorResponse(
            status  = HttpStatus.CONFLICT.value(),
            error   = "Conflict",
            message = ex.message ?: "Email already exists",
            path    = request.requestURI
        )
    )

    @ExceptionHandler(UsernameAlreadyExistsException::class)
    fun handleUsernameConflict(
        ex: UsernameAlreadyExistsException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> = ResponseEntity.status(HttpStatus.CONFLICT).body(
        ErrorResponse(
            status  = HttpStatus.CONFLICT.value(),
            error   = "Conflict",
            message = ex.message ?: "Username already taken",
            path    = request.requestURI
        )
    )

    @ExceptionHandler(InvalidCredentialsException::class)
    fun handleInvalidCredentials(
        ex: InvalidCredentialsException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
        ErrorResponse(
            status  = HttpStatus.UNAUTHORIZED.value(),
            error   = "Unauthorized",
            message = ex.message ?: "Invalid credentials",
            path    = request.requestURI
        )
    )

    // Handles @Valid annotation failures (field-level validation)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationErrors(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ResponseEntity<Map<String, Any>> {
        val errors = ex.bindingResult.allErrors.associate { error ->
            val field = (error as? FieldError)?.field ?: "global"
            field to (error.defaultMessage ?: "Invalid value")
        }
        return ResponseEntity.badRequest().body(
            mapOf(
                "status"  to HttpStatus.BAD_REQUEST.value(),
                "error"   to "Validation Failed",
                "errors"  to errors,
                "path"    to request.requestURI
            )
        )
    }
}
package digitalironbackend.example.demo.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
class EmailAlreadyExistsException(email: String) :
    RuntimeException("An account with email '$email' already exists")

@ResponseStatus(HttpStatus.CONFLICT)
class UsernameAlreadyExistsException(username: String) :
    RuntimeException("Username '$username' is already taken")

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class InvalidCredentialsException :
    RuntimeException("Invalid email or password")

@ResponseStatus(HttpStatus.NOT_FOUND)
class UserNotFoundException(id: Long) :
    RuntimeException("User with id $id not found")
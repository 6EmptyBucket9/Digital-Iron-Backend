package digitalironbackend.example.demo.repository

import digitalironbackend.example.demo.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: String): Optional<User>

    fun findByName(name: String): Optional<User>

    fun existsByEmail(email: String): Boolean

    fun existsByName(name: String): Boolean
}
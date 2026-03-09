package digitalironbackend.example.demo.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Date

@Service
class JwtService(
    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.expiration-ms:86400000}") private val expirationMs: Long
) {
    private val key = Keys.hmacShaKeyFor(secret.toByteArray())

    fun generateToken(userId: String?): String {
        return Jwts.builder()
            .setSubject(userId)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expirationMs))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun extractUserId(token: String?): String =
        Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
            .subject

    fun isValid(token: String): Boolean = runCatching { extractUserId(token); true }.getOrDefault(false)
}
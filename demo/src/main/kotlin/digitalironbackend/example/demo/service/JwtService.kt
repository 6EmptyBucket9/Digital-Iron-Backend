package digitalironbackend.example.demo.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.Date

@Service
class JwtService(
    @Value("\${application.jwt.secret}") private val secretKey: String,
    @Value("\${application.jwt.expiration}") private val jwtExpiration: Long,
    @Value("\${application.jwt.refresh-expiration}") private val refreshExpiration: Long
) {

    // ── Token Generation ──────────────────────────────────────────────────────

    fun generateAccessToken(userDetails: UserDetails): String =
        buildToken(userDetails, jwtExpiration)

    fun generateRefreshToken(userDetails: UserDetails): String =
        buildToken(userDetails, refreshExpiration)

    // jjwt 0.11.x API: setSubject / setIssuedAt / setExpiration / signWith(Key)
    private fun buildToken(userDetails: UserDetails, expiration: Long): String =
        Jwts.builder()
            .setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + expiration))
            .signWith(signingKey())
            .compact()

    // ── Token Validation ──────────────────────────────────────────────────────

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    fun extractUsername(token: String): String =
        extractClaim(token, Claims::getSubject)

    private fun isTokenExpired(token: String): Boolean =
        extractExpiration(token).before(Date())

    private fun extractExpiration(token: String): Date =
        extractClaim(token, Claims::getExpiration)

    private fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T =
        claimsResolver(extractAllClaims(token))

    // jjwt 0.11.x API: parserBuilder() → setSigningKey() → parseClaimsJws()
    private fun extractAllClaims(token: String): Claims =
        Jwts.parserBuilder()
            .setSigningKey(signingKey())
            .build()
            .parseClaimsJws(token)
            .body

    private fun signingKey(): Key =
        Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))
}
package com.somnal.app.withpark.config

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    @Value("\${jwt.access-secret}")
    private val accessSecretKey: String,
    @Value("\${jwt.refresh-secret}")
    private val refreshSecretKey: String,
    @Value("\${jwt.access-token-expiration}")
    private val accessTokenExpiration: Long,
    @Value("\${jwt.refresh-token-expiration}")
    private val refreshTokenExpiration: Long
) {
    private val accessKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(accessSecretKey.toByteArray())
    }

    private val refreshKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(refreshSecretKey.toByteArray())
    }

    fun generateAccessToken(userId: Long): String {
        return generateToken(userId, accessTokenExpiration, accessKey)
    }

    fun generateRefreshToken(userId: Long): String {
        return generateToken(userId, refreshTokenExpiration, refreshKey)
    }

    private fun generateToken(userId: Long, expiration: Long, key: SecretKey): String {
        val now = Date()
        val expiryDate = Date(now.time + expiration)

        return Jwts.builder()
            .subject(userId.toString())
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(key)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return validateAccessToken(token) || validateRefreshToken(token)
    }

    fun validateAccessToken(token: String): Boolean {
        return try {
            Jwts.parser()
                .verifyWith(accessKey)
                .build()
                .parseSignedClaims(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun validateRefreshToken(token: String): Boolean {
        return try {
            Jwts.parser()
                .verifyWith(refreshKey)
                .build()
                .parseSignedClaims(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getUserIdFromToken(token: String): Long {
        val claims = getClaimsFromToken(token)
        return claims.subject.toLong()
    }

    private fun getClaimsFromToken(token: String): Claims {
        return try {
            Jwts.parser()
                .verifyWith(accessKey)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: Exception) {
            Jwts.parser()
                .verifyWith(refreshKey)
                .build()
                .parseSignedClaims(token)
                .payload
        }
    }
}

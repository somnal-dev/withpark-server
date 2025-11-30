package com.somnal.app.withpark.domain.auth.repository

import com.somnal.app.withpark.domain.auth.entity.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {
    fun findByToken(token: String): RefreshToken?
    fun findByUserId(userId: Long): RefreshToken?
    fun deleteByUserId(userId: Long)
    fun deleteByExpiresAtBefore(dateTime: LocalDateTime)
}

package com.somnal.app.withpark.domain.auth.service

import com.somnal.app.withpark.config.JwtTokenProvider
import com.somnal.app.withpark.domain.auth.dto.TokenRefreshResponseDto
import com.somnal.app.withpark.domain.auth.entity.RefreshToken
import com.somnal.app.withpark.domain.auth.repository.RefreshTokenRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class AuthService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val refreshTokenRepository: RefreshTokenRepository,
    @Value("\${jwt.refresh-token-expiration}")
    private val refreshTokenExpiration: Long
) {
    @Transactional
    fun refreshAccessToken(refreshToken: String): TokenRefreshResponseDto {
        // 리프레시 토큰 검증
        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            throw IllegalArgumentException("유효하지 않은 리프레시 토큰입니다")
        }

        // DB에서 리프레시 토큰 조회
        val savedRefreshToken = refreshTokenRepository.findByToken(refreshToken)
            ?: throw IllegalArgumentException("리프레시 토큰을 찾을 수 없습니다")

        // 만료 시간 확인
        if (savedRefreshToken.expiresAt.isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(savedRefreshToken)
            throw IllegalArgumentException("만료된 리프레시 토큰입니다")
        }

        // 토큰에서 사용자 정보 추출
        val userId = jwtTokenProvider.getUserIdFromToken(refreshToken)
        val username = jwtTokenProvider.getUsernameFromToken(refreshToken)

        // 새로운 액세스 토큰 생성
        val newAccessToken = jwtTokenProvider.generateAccessToken(userId, username)

        // 새로운 리프레시 토큰 생성 (선택적: 리프레시 토큰도 갱신할지 결정)
        val newRefreshToken = jwtTokenProvider.generateRefreshToken(userId, username)
        val expiresAt = LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000)

        // 기존 리프레시 토큰 삭제 및 새 토큰 저장
        refreshTokenRepository.delete(savedRefreshToken)
        refreshTokenRepository.save(
            RefreshToken(
                userId = userId,
                token = newRefreshToken,
                expiresAt = expiresAt
            )
        )

        return TokenRefreshResponseDto(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )
    }
}

package com.somnal.app.withpark.domain.oauth.service

import com.somnal.app.withpark.config.JwtTokenProvider
import com.somnal.app.withpark.domain.auth.entity.RefreshToken
import com.somnal.app.withpark.domain.auth.repository.RefreshTokenRepository
import com.somnal.app.withpark.domain.oauth.dto.NaverLoginResponseDto
import com.somnal.app.withpark.domain.oauth.dto.NaverUserInfoResponseDto
import com.somnal.app.withpark.domain.user.dto.UserDto
import com.somnal.app.withpark.domain.user.entity.User
import com.somnal.app.withpark.domain.user.enumeration.SocialType
import com.somnal.app.withpark.domain.user.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestClient
import java.time.LocalDateTime

@Service
class NaverOauthService(
    private val userRepository: UserRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val refreshTokenRepository: RefreshTokenRepository,
    @Value("\${jwt.refresh-token-expiration}")
    private val refreshTokenExpiration: Long,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun login(accessToken: String): NaverLoginResponseDto {
        val naverUserInfo = RestClient.create()
            .get()
            .uri("https://openapi.naver.com/v1/nid/me")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
            .retrieve()
            .body(NaverUserInfoResponseDto::class.java)
            ?: throw Exception("네이버 로그인이 정상적으로 진행되지 않았습니다.")

        if (naverUserInfo.resultCode != "00") {
            throw Exception("네이버 로그인이 정상적으로 진행되지 않았습니다: ${naverUserInfo.message}")
        }

        // 사용자 찾기
        val username = "naver_${naverUserInfo.response.id}"
        var user = userRepository.findUserByUsername(username)

        if (user == null) {
            user = userRepository.save(
                User(
                    username = username,
                    email = naverUserInfo.response.email ?: "",
                    nickname = naverUserInfo.response.nickname ?: naverUserInfo.response.name ?: "네이버 사용자",
                    socialType = SocialType.NAVER,
                    socialId = naverUserInfo.response.id,
                )
            )
        }

        // 기존 리프레시 토큰 삭제
        refreshTokenRepository.deleteByUserId(user.id)

        // 새로운 토큰 생성
        val newAccessToken = jwtTokenProvider.generateAccessToken(user.id)
        val newRefreshToken = jwtTokenProvider.generateRefreshToken(user.id)

        // 리프레시 토큰 저장
        val expiresAt = LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000)
        refreshTokenRepository.save(
            RefreshToken(
                userId = user.id,
                token = newRefreshToken,
                expiresAt = expiresAt
            )
        )

        return NaverLoginResponseDto(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken,
            user = UserDto.fromEntity(user)
        )
    }
}

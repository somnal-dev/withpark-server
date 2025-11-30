package com.somnal.app.withpark.domain.oauth.service

import com.somnal.app.withpark.common.Const
import com.somnal.app.withpark.config.JwtTokenProvider
import com.somnal.app.withpark.domain.auth.entity.RefreshToken
import com.somnal.app.withpark.domain.auth.repository.RefreshTokenRepository
import com.somnal.app.withpark.domain.oauth.dto.KakaoLoginResponseDto
import com.somnal.app.withpark.domain.oauth.dto.KakaoTokenResponseDto
import com.somnal.app.withpark.domain.oauth.dto.KakaoUserInfoResponseDto
import com.somnal.app.withpark.domain.user.dto.UserDto
import com.somnal.app.withpark.domain.user.entity.User
import com.somnal.app.withpark.domain.user.enumeration.SocialType
import com.somnal.app.withpark.domain.user.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestClient
import java.time.LocalDateTime

@Service
class KakaoOauthService(
    private val userRepository: UserRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val refreshTokenRepository: RefreshTokenRepository,
    @Value("\${jwt.refresh-token-expiration}")
    private val refreshTokenExpiration: Long
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Value("\${kakao.REST_API_KEY}")
    private val restApiKey: String = ""

    fun getAccessToken(code: String): String {
        val kakaoTokenResponse = RestClient.create()
            .post()
            .uri(Const.KAKAO_GET_ACCESS_TOKEN_URL) { uriBuilder ->
                uriBuilder
                    .queryParam("grant_type", "authorization_code")
                    .queryParam("client_id", restApiKey)
                    .queryParam("code", code)
                    .build()
            }
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .retrieve()
            .body(KakaoTokenResponseDto::class.java)

        return kakaoTokenResponse?.accessToken ?: ""
    }

    @Transactional
    fun login(accessToken: String): KakaoLoginResponseDto {
        val kakaoUserInfo = RestClient.create()
            .get()
            .uri(Const.KAKAO_GET_USER_INFO_URL)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
            .retrieve()
            .body(KakaoUserInfoResponseDto::class.java) ?: throw Exception("카카오 로그인이 정상적으로 진행되지 않았습니다.")

        // 사용자 찾기
        val username = "kakao_${kakaoUserInfo.id}"
        var user = userRepository.findUserByUsername(username)

        if(user == null) {
            user = userRepository.save(
                User(
                    username = username,
                    email = kakaoUserInfo.kakaoAccount.email,
                    nickname = kakaoUserInfo.kakaoAccount.profile.nickName,
                    socialType = SocialType.KAKAO,
                    socialId = kakaoUserInfo.id.toString(),
                )
            )
        }

        // 기존 리프레시 토큰 삭제
        refreshTokenRepository.deleteByUserId(user.id)

        // 새로운 토큰 생성
        val newAccessToken = jwtTokenProvider.generateAccessToken(user.id, user.username)
        val newRefreshToken = jwtTokenProvider.generateRefreshToken(user.id, user.username)

        // 리프레시 토큰 저장
        val expiresAt = LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000)
        refreshTokenRepository.save(
            RefreshToken(
                userId = user.id,
                token = newRefreshToken,
                expiresAt = expiresAt
            )
        )

        return KakaoLoginResponseDto(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken,
            user = UserDto.fromEntity(user)
        )
    }
}
package com.somnal.app.withpark.domain.oauth.service

import com.somnal.app.withpark.constant.Const
import com.somnal.app.withpark.domain.oauth.dto.KakaoTokenResponseDto
import com.somnal.app.withpark.domain.oauth.dto.KakaoUserInfoResponseDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class KakaoOauthService {
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

    fun getUserInfo(accessToken: String): KakaoUserInfoResponseDto? {

        try {
            val kakaoUserInfoResponse = RestClient.create()
                .get()
                .uri(Const.KAKAO_GET_USER_INFO_URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
                .retrieve()
                .body(KakaoUserInfoResponseDto::class.java)

            return kakaoUserInfoResponse
        } catch (e: Exception) {
            log.info("에러 : ${e.stackTraceToString()}")
        }
        return null

    }
}
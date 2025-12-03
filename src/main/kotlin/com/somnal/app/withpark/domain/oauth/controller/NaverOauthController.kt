package com.somnal.app.withpark.domain.oauth.controller

import com.somnal.app.withpark.common.ApiResponse
import com.somnal.app.withpark.common.Const
import com.somnal.app.withpark.domain.oauth.dto.KakaoLoginResponseDto
import com.somnal.app.withpark.domain.oauth.dto.NaverLoginResponseDto
import com.somnal.app.withpark.domain.oauth.service.NaverOauthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@Tag(name = "네이버 로그인 API", description = "네이버 로그인을 처리하는 API")
@RestController
@RequestMapping("/api/oauth/naver")
class NaverOauthController(
    private val naverOauthService: NaverOauthService,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Value("\${naver.REST_API_KEY}")
    private val restApiKey: String? = null

    @Value("\${naver.REDIRECT_URI}")
    private val redirectUri: String? = null

    @Value("\${naver.OAUTH_CALLBACK_URL}")
    private val oauthCallbackUri: String? = null

    @Operation(summary = "네이버 인가코드 요청 (소셜로그인 페이지 이동) API")
    @GetMapping("/authorize")
    fun authorizeNaver(): ResponseEntity<Void> {
        val url = "${Const.NAVER_AUTHORIZE_URL}?response_type=code" +
                "&client_id=${restApiKey}" +
                "&redirect_uri=${redirectUri}"

        return ResponseEntity
            .status(HttpStatus.FOUND)
            .location(URI.create(url))
            .build()
    }

    @Operation(summary = "네이버 인가코드 응답수신 API")
    @GetMapping("/callback")
    fun authorizeKakaoCallback(
        @RequestParam("code") code: String
    ): ResponseEntity<Void> {
        // 인가코드를 통한 액세스토큰 수신
        val accessToken = naverOauthService.getAccessToken(code)

        val url = "${oauthCallbackUri}?access_token=${accessToken}"

        return ResponseEntity
            .status(HttpStatus.FOUND)
            .location(URI.create(url))
            .build()
    }

    @Operation(summary = "액세스 코드를 통한 소셜로그인 API")
    @GetMapping("/login")
    fun naverLogin(
        @RequestParam("access_token") accessToken: String
    ): ResponseEntity<ApiResponse<NaverLoginResponseDto>> {
        val userInfo = naverOauthService.login(accessToken)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = Const.RESULT_MESSAGE_SUCCESS,
                data = userInfo
            )
        )
    }

}

package com.somnal.app.withpark.domain.oauth.controller

import com.somnal.app.withpark.common.ApiResponse
import com.somnal.app.withpark.constant.Const
import com.somnal.app.withpark.domain.oauth.dto.KakaoUserInfoResponseDto
import com.somnal.app.withpark.domain.oauth.service.KakaoOauthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@Tag(name = "카카오 로그인 API", description = "카카오 로그인을 처리하는 API")
@RestController
@RequestMapping("/api/oauth/kakao")
class KakaoOauthController(
    val kakaoOauthService: KakaoOauthService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Value("\${kakao.REST_API_KEY}")
    private val restApiKey: String? = null

    @Value("\${kakao.REDIRECT_URI}")
    private val redirectUri: String? = null

    @Operation(summary = "카카오 로그인 페이지 리다이렉션 API")
    @GetMapping("/login")
    fun kakaoLogin(model: Model): ResponseEntity<Void> {
        log.info("카카오 로그인 요청")

        val url = "${Const.KAKAO_AUTHORIZE_URL}?response_type=code&client_id=${restApiKey}&redirect_uri=${redirectUri}"

        return ResponseEntity
            .status(HttpStatus.FOUND)
            .location(URI.create(url))
            .build()
    }

    @Operation(summary = "카카오 인가코드 응답수신 API")
    @GetMapping("/callback")
    fun callback(
        @RequestParam("code") code: String = ""
    ): ResponseEntity<ApiResponse<KakaoUserInfoResponseDto>> {

        // 인가코드를 통한 액세스토큰 수신
        val accessToken = kakaoOauthService.getAccessToken(code)

        log.info("액세스 토큰 : $accessToken")

        val kakaoUserInfo = kakaoOauthService.getUserInfo(accessToken)

        log.info("카카오 사용자 정보 : $kakaoUserInfo")

        return ResponseEntity.ok(
            ApiResponse.success(
                message = Const.RESULT_MESSAGE_SUCCESS,
                data = kakaoUserInfo
            )
        )
    }
}
package com.somnal.app.withpark.domain.oauth.controller

import com.somnal.app.withpark.common.ApiResponse
import com.somnal.app.withpark.domain.oauth.dto.NaverLoginResponseDto
import com.somnal.app.withpark.domain.oauth.service.NaverOauthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "네이버 OAuth API", description = "네이버 소셜 로그인 API")
@RestController
@RequestMapping("/api/oauth/naver")
class NaverOauthController(
    private val naverOauthService: NaverOauthService,
) {
    @Operation(summary = "네이버 로그인 API")
    @GetMapping("/login")
    fun login(
        @Parameter(description = "네이버 Access Token", required = true)
        @RequestParam("access_token") accessToken: String,
    ): ResponseEntity<ApiResponse<NaverLoginResponseDto>> {
        val loginResponse = naverOauthService.login(accessToken)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = "네이버 로그인에 성공했습니다",
                data = loginResponse
            )
        )
    }
}

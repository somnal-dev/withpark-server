package com.somnal.app.withpark.domain.auth.controller

import com.somnal.app.withpark.common.ApiResponse
import com.somnal.app.withpark.common.Const
import com.somnal.app.withpark.domain.auth.dto.TokenRefreshRequestDto
import com.somnal.app.withpark.domain.auth.dto.TokenRefreshResponseDto
import com.somnal.app.withpark.domain.auth.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "인증 API", description = "인증 관련 API")
@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {
    @Operation(summary = "토큰 갱신 API")
    @PostMapping("/refresh")
    fun refreshToken(
        @RequestBody request: TokenRefreshRequestDto
    ): ResponseEntity<ApiResponse<TokenRefreshResponseDto>> {
        val tokens = authService.refreshAccessToken(request.refreshToken)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = Const.RESULT_MESSAGE_SUCCESS,
                data = tokens
            )
        )
    }
}

package com.somnal.app.withpark.domain.oauth.dto

import com.somnal.app.withpark.domain.user.dto.UserDto
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "네이버 로그인 응답 DTO")
data class NaverLoginResponseDto(
    @Schema(description = "Access Token")
    val accessToken: String,

    @Schema(description = "Refresh Token")
    val refreshToken: String,

    @Schema(description = "사용자 정보")
    val user: UserDto,
)

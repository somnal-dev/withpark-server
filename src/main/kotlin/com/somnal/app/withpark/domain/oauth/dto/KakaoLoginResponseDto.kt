package com.somnal.app.withpark.domain.oauth.dto

import com.somnal.app.withpark.domain.user.dto.UserDto

data class KakaoLoginResponseDto (
    val accessToken: String,
    val refreshToken: String,
    val user: UserDto,
)
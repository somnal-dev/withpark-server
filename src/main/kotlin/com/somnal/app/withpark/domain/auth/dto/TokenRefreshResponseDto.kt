package com.somnal.app.withpark.domain.auth.dto

data class TokenRefreshResponseDto(
    val accessToken: String,
    val refreshToken: String
)

package com.somnal.app.withpark.domain.oauth.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class NaverUserInfoResponseDto(
    @JsonProperty("resultcode")
    val resultCode: String,
    val message: String,
    val response: NaverUserInfo,
)

data class NaverUserInfo(
    val id: String,
    val email: String?,
    val nickname: String?,
    @JsonProperty("profile_image")
    val profileImage: String?,
    val name: String?,
)

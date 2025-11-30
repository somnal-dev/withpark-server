package com.somnal.app.withpark.domain.user.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "사용자 정보 변경 요청 DTO")
data class UpdateUserRequestDto(
    @Schema(description = "닉네임", example = "홍길동")
    val nickname: String? = null,
    @Schema(description = "온보딩 완료여부", example = "true")
    val onboardingDone: Boolean? = null,
    @Schema(description = "자기소개", example = "안녕하세요")
    val introduction: String? = null,
)
package com.somnal.app.withpark.domain.user.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "사용자 생성 요청 DTO")
data class CreateUserRequest(
    @field:NotBlank(message = "사용자명은 필수입니다.")
    val username: String
)
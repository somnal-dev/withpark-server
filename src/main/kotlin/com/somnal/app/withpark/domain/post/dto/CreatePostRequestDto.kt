package com.somnal.app.withpark.domain.post.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "게시글 생성 요청 DTO")
data class CreatePostRequestDto(
    @Schema(description = "게시글 제목", example = "골프장 후기")
    @field:NotBlank(message = "제목은 필수입니다")
    val title: String,

    @Schema(description = "게시글 내용", example = "오늘 다녀온 골프장 정말 좋았어요!")
    @field:NotBlank(message = "내용은 필수입니다")
    val content: String,
)

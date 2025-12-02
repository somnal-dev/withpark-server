package com.somnal.app.withpark.domain.comment.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "댓글 수정 요청 DTO")
data class UpdateCommentRequestDto(
    @Schema(description = "댓글 내용", example = "수정된 댓글 내용")
    @field:NotBlank(message = "내용은 필수입니다")
    val content: String,
)

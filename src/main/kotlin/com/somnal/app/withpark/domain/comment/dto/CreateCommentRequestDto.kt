package com.somnal.app.withpark.domain.comment.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@Schema(description = "댓글 생성 요청 DTO")
data class CreateCommentRequestDto(
    @Schema(description = "댓글 내용", example = "좋은 글 감사합니다!")
    @field:NotBlank(message = "내용은 필수입니다")
    val content: String,

    @Schema(description = "게시글 ID", example = "1")
    @field:NotNull(message = "게시글 ID는 필수입니다")
    val postId: Long,
)

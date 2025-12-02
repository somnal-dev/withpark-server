package com.somnal.app.withpark.domain.post.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "게시글 수정 요청 DTO")
data class UpdatePostRequestDto(
    @Schema(description = "게시글 제목", example = "수정된 제목")
    val title: String?,

    @Schema(description = "게시글 내용", example = "수정된 내용")
    val content: String?,
)

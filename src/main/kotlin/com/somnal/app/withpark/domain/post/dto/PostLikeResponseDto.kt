package com.somnal.app.withpark.domain.post.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "게시글 좋아요 응답 DTO")
data class PostLikeResponseDto(
    @Schema(description = "게시글 ID", example = "1")
    val postId: Long,

    @Schema(description = "좋아요 액션 (like/unlike)", example = "like")
    val action: String,

    @Schema(description = "좋아요 수", example = "10")
    val likeCount: Long,

    @Schema(description = "좋아요 여부", example = "true")
    val isLiked: Boolean,
)

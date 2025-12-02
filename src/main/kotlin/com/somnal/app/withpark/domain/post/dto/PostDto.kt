package com.somnal.app.withpark.domain.post.dto

import com.somnal.app.withpark.domain.post.entity.Post
import com.somnal.app.withpark.domain.user.dto.UserDto
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "게시글 정보 DTO")
data class PostDto(
    val id: Long,
    val documentId: String,
    val title: String,
    val content: String,
    val viewCount: Long,
    val likeCount: Long,
    val commentCount: Long,
    val user: UserDto,
    val images: List<ImageDto> = emptyList(),
    val isLiked: Boolean = false,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
) {
    companion object {
        fun fromEntity(post: Post, images: List<ImageDto> = emptyList(), isLiked: Boolean = false): PostDto =
            PostDto(
                id = post.id,
                documentId = post.id.toString(),
                title = post.title,
                content = post.content,
                viewCount = post.viewCount,
                likeCount = post.likeCount,
                commentCount = post.commentCount,
                user = UserDto.fromEntity(post.user),
                images = images,
                isLiked = isLiked,
                createdAt = post.createdAt,
                updatedAt = post.updatedAt
            )
    }
}

@Schema(description = "이미지 정보 DTO")
data class ImageDto(
    val id: Long,
    val url: String,
    val name: String,
    val size: Long,
    val width: Int?,
    val height: Int?,
    val mimeType: String,
)

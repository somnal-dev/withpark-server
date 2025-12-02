package com.somnal.app.withpark.domain.comment.dto

import com.somnal.app.withpark.domain.comment.entity.Comment
import com.somnal.app.withpark.domain.user.dto.UserDto
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "댓글 정보 DTO")
data class CommentDto(
    val id: Long,
    val documentId: String,
    val content: String,
    val user: UserDto?,
    val postId: Long,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
) {
    companion object {
        fun fromEntity(comment: Comment): CommentDto =
            CommentDto(
                id = comment.id,
                documentId = comment.id.toString(),
                content = comment.content,
                user = UserDto.fromEntity(comment.user),
                postId = comment.post.id,
                createdAt = comment.createdAt,
                updatedAt = comment.updatedAt
            )
    }
}

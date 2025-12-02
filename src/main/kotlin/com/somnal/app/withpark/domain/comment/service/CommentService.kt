package com.somnal.app.withpark.domain.comment.service

import com.somnal.app.withpark.domain.comment.dto.CommentDto
import com.somnal.app.withpark.domain.comment.dto.CreateCommentRequestDto
import com.somnal.app.withpark.domain.comment.dto.UpdateCommentRequestDto
import com.somnal.app.withpark.domain.comment.entity.Comment
import com.somnal.app.withpark.domain.comment.repository.CommentRepository
import com.somnal.app.withpark.domain.post.repository.PostRepository
import com.somnal.app.withpark.domain.user.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CommentService(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
) {
    fun getComments(postId: Long, page: Int, pageSize: Int): Page<CommentDto> {
        val pageable: Pageable = PageRequest.of(page - 1, pageSize)
        val comments = commentRepository.findByPostIdWithUser(postId, pageable)

        return comments.map { CommentDto.fromEntity(it) }
    }

    @Transactional
    fun createComment(userId: Long, request: CreateCommentRequestDto): CommentDto {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("사용자를 찾을 수 없습니다") }

        val post = postRepository.findById(request.postId)
            .orElseThrow { IllegalArgumentException("게시글을 찾을 수 없습니다") }

        val comment = Comment(
            content = request.content,
            user = user,
            post = post
        )

        val savedComment = commentRepository.save(comment)

        // 게시글의 댓글 수 증가
        post.commentCount++
        postRepository.save(post)

        return CommentDto.fromEntity(savedComment)
    }

    @Transactional
    fun updateComment(commentId: Long, userId: Long, request: UpdateCommentRequestDto): CommentDto {
        val comment = commentRepository.findById(commentId)
            .orElseThrow { IllegalArgumentException("댓글을 찾을 수 없습니다") }

        if (comment.user.id != userId) {
            throw IllegalArgumentException("댓글 수정 권한이 없습니다")
        }

        comment.content = request.content

        val updatedComment = commentRepository.save(comment)

        return CommentDto.fromEntity(updatedComment)
    }

    @Transactional
    fun deleteComment(commentId: Long, userId: Long) {
        val comment = commentRepository.findById(commentId)
            .orElseThrow { IllegalArgumentException("댓글을 찾을 수 없습니다") }

        if (comment.user.id != userId) {
            throw IllegalArgumentException("댓글 삭제 권한이 없습니다")
        }

        val post = comment.post
        post.commentCount = maxOf(0, post.commentCount - 1)
        postRepository.save(post)

        commentRepository.delete(comment)
    }
}

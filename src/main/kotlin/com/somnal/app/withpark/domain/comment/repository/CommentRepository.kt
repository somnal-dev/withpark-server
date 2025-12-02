package com.somnal.app.withpark.domain.comment.repository

import com.somnal.app.withpark.domain.comment.entity.Comment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CommentRepository : JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c JOIN FETCH c.user JOIN FETCH c.post WHERE c.post.id = :postId ORDER BY c.createdAt ASC")
    fun findByPostIdWithUser(postId: Long, pageable: Pageable): Page<Comment>

    fun countByPostId(postId: Long): Long

    fun deleteByPostId(postId: Long)
}

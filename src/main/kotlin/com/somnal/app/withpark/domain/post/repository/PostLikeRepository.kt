package com.somnal.app.withpark.domain.post.repository

import com.somnal.app.withpark.domain.post.entity.PostLike
import org.springframework.data.jpa.repository.JpaRepository

interface PostLikeRepository : JpaRepository<PostLike, Long> {
    fun existsByPostIdAndUserId(postId: Long, userId: Long): Boolean
    fun findByPostIdAndUserId(postId: Long, userId: Long): PostLike?
    fun countByPostId(postId: Long): Long
    fun deleteByPostIdAndUserId(postId: Long, userId: Long)
}

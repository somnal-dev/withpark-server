package com.somnal.app.withpark.domain.post.repository

import com.somnal.app.withpark.domain.post.entity.PostImage
import org.springframework.data.jpa.repository.JpaRepository

interface PostImageRepository : JpaRepository<PostImage, Long> {
    fun findByPostId(postId: Long): List<PostImage>
    fun deleteByPostId(postId: Long)
}

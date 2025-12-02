package com.somnal.app.withpark.domain.post.repository

import com.somnal.app.withpark.domain.post.entity.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PostRepository : JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p JOIN FETCH p.user ORDER BY p.createdAt DESC, p.id DESC")
    fun findAllWithUser(pageable: Pageable): Page<Post>

    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword% ORDER BY p.createdAt DESC, p.id DESC")
    fun searchByTitle(keyword: String, pageable: Pageable): Page<Post>

    @Query("SELECT p FROM Post p JOIN FETCH p.user ORDER BY p.viewCount DESC, p.id DESC")
    fun findAllOrderByViewCount(pageable: Pageable): Page<Post>

    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.id = :id")
    fun findByIdWithUser(id: Long): Post?
}

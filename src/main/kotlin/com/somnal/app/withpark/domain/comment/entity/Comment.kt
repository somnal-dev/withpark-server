package com.somnal.app.withpark.domain.comment.entity

import com.somnal.app.withpark.common.BaseEntity
import com.somnal.app.withpark.domain.post.entity.Post
import com.somnal.app.withpark.domain.user.entity.User
import jakarta.persistence.*

@Entity
@Table(name = "comments")
data class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(columnDefinition = "TEXT")
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    val post: Post,
) : BaseEntity()

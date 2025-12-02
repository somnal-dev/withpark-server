package com.somnal.app.withpark.domain.post.entity

import com.somnal.app.withpark.common.BaseEntity
import com.somnal.app.withpark.domain.user.entity.User
import jakarta.persistence.*

@Entity
@Table(
    name = "post_likes",
    uniqueConstraints = [UniqueConstraint(columnNames = ["post_id", "user_id"])]
)
data class PostLike(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    val post: Post,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,
) : BaseEntity()

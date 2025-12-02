package com.somnal.app.withpark.domain.post.entity

import com.somnal.app.withpark.common.BaseEntity
import com.somnal.app.withpark.domain.comment.entity.Comment
import com.somnal.app.withpark.domain.user.entity.User
import jakarta.persistence.*

@Entity
@Table(name = "posts")
data class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var title: String,

    @Column(columnDefinition = "TEXT")
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    var viewCount: Long = 0,

    var likeCount: Long = 0,

    var commentCount: Long = 0,

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
    val comments: MutableList<Comment> = mutableListOf(),

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
    val likes: MutableList<PostLike> = mutableListOf(),
) : BaseEntity()

package com.somnal.app.withpark.domain.user.entity

import com.somnal.app.withpark.common.BaseEntity
import com.somnal.app.withpark.domain.comment.entity.Comment
import com.somnal.app.withpark.domain.post.entity.Post
import com.somnal.app.withpark.domain.post.entity.PostLike
import com.somnal.app.withpark.domain.user.enumeration.SocialType
import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val username: String,
    val email: String,

    var nickname: String,
    @Enumerated(EnumType.STRING)
    val socialType: SocialType,
    val socialId: String? = null,
    var introduction: String? = null,
    var photoUrl: String? = null,

    var onboardingDone: Boolean = false,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    val posts: MutableList<Post> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    val comments: MutableList<Comment> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    val postLikes: MutableList<PostLike> = mutableListOf(),
): BaseEntity()

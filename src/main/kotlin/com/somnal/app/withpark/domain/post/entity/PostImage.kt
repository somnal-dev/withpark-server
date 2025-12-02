package com.somnal.app.withpark.domain.post.entity

import com.somnal.app.withpark.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "post_images")
data class PostImage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    val post: Post,

    val url: String,

    val name: String,

    val size: Long,

    val width: Int? = null,

    val height: Int? = null,

    val mimeType: String,

    @Column(name = "display_order")
    val order: Int = 0,
) : BaseEntity()

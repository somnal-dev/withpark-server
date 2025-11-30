package com.somnal.app.withpark.domain.auth.entity

import com.somnal.app.withpark.common.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "refresh_tokens")
class RefreshToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val userId: Long,

    @Column(nullable = false, unique = true, length = 500)
    val token: String,

    @Column(nullable = false)
    val expiresAt: LocalDateTime
) : BaseEntity()

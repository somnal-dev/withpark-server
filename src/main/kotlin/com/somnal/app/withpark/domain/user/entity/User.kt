package com.somnal.app.withpark.domain.user.entity

import com.somnal.app.withpark.common.BaseEntity
import com.somnal.app.withpark.domain.user.enumeration.SocialType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

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
): BaseEntity()

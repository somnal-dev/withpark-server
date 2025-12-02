package com.somnal.app.withpark.domain.user.dto

import com.somnal.app.withpark.domain.user.entity.User
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "사용자 정보 DTO")
data class UserDto(
    val id: Long = 0,
    val username: String,
    val email: String,

    val nickname: String,
    val introduction: String? = null,
    val photo: PhotoDto? = null,

    val onboardingDone: Boolean = false,
) {
    companion object {
        fun fromEntity(user: User): UserDto =
            UserDto(
                id = user.id,
                username = user.username,
                email = user.email,
                nickname = user.nickname,
                introduction = user.introduction,
                photo = user.photoUrl?.let { PhotoDto(url = it) },
                onboardingDone = user.onboardingDone
            )
    }
}

@Schema(description = "사진 정보 DTO")
data class PhotoDto(
    val url: String,
)
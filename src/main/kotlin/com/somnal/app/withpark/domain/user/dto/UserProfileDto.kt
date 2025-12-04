package com.somnal.app.withpark.domain.user.dto

import com.somnal.app.withpark.domain.user.entity.User
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "사용자 프로필 DTO")
data class UserProfileDto(
    @Schema(description = "사용자 ID", example = "1")
    val id: Long,

    @Schema(description = "닉네임", example = "골프왕")
    val nickname: String,

    @Schema(description = "프로필 사진")
    val photo: PhotoDto? = null,

    @Schema(description = "자기소개", example = "골프를 사랑하는 사람입니다")
    val introduction: String? = null,
) {
    companion object {
        fun fromEntity(user: User): UserProfileDto =
            UserProfileDto(
                id = user.id,
                nickname = user.nickname,
                photo = user.photoUrl?.let {
                    // 기존 전체 URL을 상대경로로 변환
                    val relativePath = it.replace(Regex("^https?://[^/]+"), "")
                    PhotoDto(url = relativePath)
                },
                introduction = user.introduction
            )
    }
}

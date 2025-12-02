package com.somnal.app.withpark.domain.user.service

import com.somnal.app.withpark.domain.user.dto.UpdateUserRequestDto
import com.somnal.app.withpark.domain.user.dto.UserDto
import com.somnal.app.withpark.domain.user.dto.UserProfileDto
import com.somnal.app.withpark.domain.user.repository.UserRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun findUserByUsername(username: String): UserDto {
        val user = userRepository.findUserByUsername(username)
            ?: throw UsernameNotFoundException("사용자를 찾을 수 없습니다: $username")

        return UserDto.fromEntity(user)
    }

    fun findUserById(userId: Long): UserDto {
        val user = userRepository.findById(userId)
            .orElseThrow { UsernameNotFoundException("사용자를 찾을 수 없습니다: $userId") }

        return UserDto.fromEntity(user)
    }

    fun findUserProfileById(userId: Long): UserProfileDto {
        val user = userRepository.findById(userId)
            .orElseThrow { UsernameNotFoundException("사용자를 찾을 수 없습니다: $userId") }

        return UserProfileDto.fromEntity(user)
    }

    fun updateUser(
        userId: Long,
        request: UpdateUserRequestDto
    ): UserDto {
        val user = userRepository.findById(userId)
            .orElseThrow { UsernameNotFoundException("사용자를 찾을 수 없습니다: $userId") }

        user.apply {
            onboardingDone = request.onboardingDone ?: user.onboardingDone
            nickname = request.nickname ?: user.nickname
            introduction = request.introduction ?: user.introduction
            photoUrl = request.photoUrl ?: user.photoUrl
        }

        val updatedUser = userRepository.save(user)

        return UserDto.fromEntity(updatedUser)
    }
}
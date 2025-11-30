package com.somnal.app.withpark.domain.user.service

import com.somnal.app.withpark.domain.user.dto.UserDto
import com.somnal.app.withpark.domain.user.repository.UserRepository
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
}
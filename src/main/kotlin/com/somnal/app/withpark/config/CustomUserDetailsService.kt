package com.somnal.app.withpark.config

import com.somnal.app.withpark.domain.user.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val userId = username.toLongOrNull()
            ?: throw UsernameNotFoundException("유효하지 않은 사용자 ID입니다: $username")

        val user = userRepository.findById(userId)
            .orElseThrow { UsernameNotFoundException("사용자를 찾을 수 없습니다: $userId") }

        return User.builder()
            .username(user.id.toString())
            .password("")
            .authorities(emptyList())
            .build()
    }
}

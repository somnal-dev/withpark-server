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
        val user = userRepository.findUserByUsername(username)
            ?: throw UsernameNotFoundException("사용자를 찾을 수 없습니다 : $username")

        return User.builder()
            .username(user.username)
            .password("")
            .authorities(emptyList())
            .build()
    }
}

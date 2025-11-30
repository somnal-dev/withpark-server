package com.somnal.app.withpark.domain.user.repository

import com.somnal.app.withpark.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long> {
}
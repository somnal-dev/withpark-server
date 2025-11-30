package com.somnal.app.withpark.domain.role.repository

import com.somnal.app.withpark.domain.role.entity.Role
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface RoleRepository: JpaRepository<Role, Long> {
    fun findByName(name: String): Optional<Role>
}

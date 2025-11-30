package com.somnal.app.withpark.domain.user.controller

import com.somnal.app.withpark.domain.user.dto.CreateUserRequest
import com.somnal.app.withpark.domain.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*


@Tag(name = "사용자 API", description = "사용자 관련 API")
@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {
    @Operation(summary = "회원가입 API")
    @PostMapping("/register")
    fun register(
        @Valid @RequestBody request: CreateUserRequest
    ) {
    }
}
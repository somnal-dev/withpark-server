package com.somnal.app.withpark.domain.user.controller

import com.somnal.app.withpark.common.ApiResponse
import com.somnal.app.withpark.common.Const
import com.somnal.app.withpark.domain.user.dto.UserDto
import com.somnal.app.withpark.domain.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*


@Tag(name = "사용자 API", description = "사용자 관련 API")
@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {
    @Operation(summary = "현재 로그인한 사용자 정보 조회 API")
    @GetMapping("/me")
    fun me(@AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<ApiResponse<UserDto>> {
        val user = userService.findUserByUsername(userDetails.username)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = Const.RESULT_MESSAGE_SUCCESS,
                data = user
            )
        )
    }
}
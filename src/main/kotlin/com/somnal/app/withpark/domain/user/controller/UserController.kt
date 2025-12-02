package com.somnal.app.withpark.domain.user.controller

import com.somnal.app.withpark.common.ApiResponse
import com.somnal.app.withpark.common.Const
import com.somnal.app.withpark.domain.user.dto.UpdateUserRequestDto
import com.somnal.app.withpark.domain.user.dto.UserDto
import com.somnal.app.withpark.domain.user.dto.UserProfileDto
import com.somnal.app.withpark.domain.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
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
    fun me(
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<ApiResponse<UserDto>> {
        val userId = userDetails.username.toLong()
        val user = userService.findUserById(userId)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = Const.RESULT_MESSAGE_SUCCESS,
                data = user
            )
        )
    }

    @Operation(summary = "사용자 프로필 조회 API")
    @GetMapping("/{userId}")
    fun getUserProfile(
        @Parameter(description = "사용자 ID", example = "1")
        @PathVariable userId: Long,
    ): ResponseEntity<ApiResponse<UserProfileDto>> {
        val userProfile = userService.findUserProfileById(userId)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = Const.RESULT_MESSAGE_SUCCESS,
                data = userProfile
            )
        )
    }

    @Operation(summary = "사용자 정보 변경 API")
    @PutMapping("/{userId}")
    fun updateUser(
        @Parameter(description = "사용자 ID", example = "1")
        @PathVariable userId: Long,
        @Valid @RequestBody request: UpdateUserRequestDto,
    ): ResponseEntity<ApiResponse<UserDto>> {

        val updatedUser = userService.updateUser(userId, request)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = Const.RESULT_MESSAGE_SUCCESS,
                data = updatedUser
            ),
        )
    }

    @Operation(summary = "회원 탈퇴 API")
    @DeleteMapping("/{userId}")
    fun deleteUser(
        @Parameter(description = "사용자 ID", example = "1")
        @PathVariable userId: Long,
    ): ResponseEntity<ApiResponse<Unit>> {
        userService.deleteUser(userId)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = "회원 탈퇴가 완료되었습니다",
                data = null
            )
        )
    }
}
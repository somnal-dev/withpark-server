package com.somnal.app.withpark.common

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "API 응답 DTO")
data class ApiResponse<T>(
    @Schema(description = "성공 여부", example = "true")
    val success: Boolean,
    @Schema(description = "응답 메시지", example = "성공적으로 처리되었습니다")
    val message: String,
    @Schema(description = "응답 데이터")
    val data: T? = null,
) {
    companion object {
        fun <T> success(
            message: String,
            data: T? = null,
        ): ApiResponse<T> = ApiResponse(true, message, data)

        fun <T> error(message: String): ApiResponse<T> = ApiResponse(false, message)
    }
}

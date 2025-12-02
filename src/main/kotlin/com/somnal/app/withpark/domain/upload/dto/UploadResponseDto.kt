package com.somnal.app.withpark.domain.upload.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "업로드 응답 DTO")
data class UploadResponseDto(
    @Schema(description = "파일 ID", example = "1")
    val id: Long,

    @Schema(description = "파일명", example = "image.jpg")
    val name: String,

    @Schema(description = "파일 URL", example = "https://example.com/uploads/image.jpg")
    val url: String,

    @Schema(description = "파일 크기 (bytes)", example = "102400")
    val size: Long,

    @Schema(description = "파일 너비 (이미지인 경우)", example = "1920")
    val width: Int?,

    @Schema(description = "파일 높이 (이미지인 경우)", example = "1080")
    val height: Int?,

    @Schema(description = "MIME 타입", example = "image/jpeg")
    val mime: String,

    @Schema(description = "파일 해시", example = "abc123def456")
    val hash: String,

    @Schema(description = "파일 확장자", example = ".jpg")
    val ext: String,
)

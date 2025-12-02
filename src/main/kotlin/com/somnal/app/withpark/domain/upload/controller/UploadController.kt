package com.somnal.app.withpark.domain.upload.controller

import com.somnal.app.withpark.common.ApiResponse
import com.somnal.app.withpark.common.Const
import com.somnal.app.withpark.domain.upload.dto.UploadResponseDto
import com.somnal.app.withpark.domain.upload.service.UploadService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Tag(name = "파일 업로드 API", description = "파일 업로드 관련 API")
@RestController
@RequestMapping("/api/upload")
class UploadController(
    private val uploadService: UploadService,
) {
    @Operation(summary = "파일 업로드 API")
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadFiles(
        @RequestParam("files") files: List<MultipartFile>,
    ): ResponseEntity<ApiResponse<List<UploadResponseDto>>> {
        val uploadedFiles = uploadService.uploadFiles(files)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = "파일이 업로드되었습니다",
                data = uploadedFiles
            )
        )
    }
}

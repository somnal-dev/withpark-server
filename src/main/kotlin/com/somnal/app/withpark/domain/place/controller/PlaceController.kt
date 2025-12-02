package com.somnal.app.withpark.domain.place.controller

import com.somnal.app.withpark.common.ApiResponse
import com.somnal.app.withpark.common.Const
import com.somnal.app.withpark.domain.place.dto.PlaceDto
import com.somnal.app.withpark.domain.place.service.PlaceService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "골프장 API", description = "골프장 관련 API")
@RestController
@RequestMapping("/api/places")
class PlaceController(
    private val placeService: PlaceService,
) {
    @Operation(summary = "골프장 목록 조회 API")
    @GetMapping
    fun getPlaces(
        @Parameter(description = "페이지 번호 (1부터 시작)", example = "1")
        @RequestParam(defaultValue = "1") page: Int,
        @Parameter(description = "페이지 크기", example = "10")
        @RequestParam(defaultValue = "10") pageSize: Int,
        @Parameter(description = "정렬 기준 (createdAt:desc, likeCount:desc)", example = "createdAt:desc")
        @RequestParam(required = false) sort: String?,
        @Parameter(description = "지역 필터", example = "서울")
        @RequestParam(required = false) area: String?,
        @Parameter(description = "검색 키워드", example = "파크")
        @RequestParam(required = false) search: String?,
    ): ResponseEntity<ApiResponse<Map<String, Any>>> {
        val places: Page<PlaceDto> = placeService.getPlaces(page, pageSize, sort, area, search)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = Const.RESULT_MESSAGE_SUCCESS,
                data = mapOf(
                    "data" to places.content,
                    "meta" to mapOf(
                        "pagination" to mapOf(
                            "page" to places.number + 1,
                            "pageSize" to places.size,
                            "pageCount" to places.totalPages,
                            "total" to places.totalElements
                        )
                    )
                )
            )
        )
    }

    @Operation(summary = "골프장 상세 조회 API")
    @GetMapping("/{documentId}")
    fun getPlace(
        @Parameter(description = "골프장 ID", example = "1")
        @PathVariable documentId: Long,
    ): ResponseEntity<ApiResponse<PlaceDto>> {
        val place = placeService.getPlace(documentId)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = Const.RESULT_MESSAGE_SUCCESS,
                data = place
            )
        )
    }
}

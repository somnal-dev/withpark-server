package com.somnal.app.withpark.domain.comment.controller

import com.somnal.app.withpark.common.ApiResponse
import com.somnal.app.withpark.common.Const
import com.somnal.app.withpark.domain.comment.dto.CommentDto
import com.somnal.app.withpark.domain.comment.dto.CreateCommentRequestDto
import com.somnal.app.withpark.domain.comment.dto.UpdateCommentRequestDto
import com.somnal.app.withpark.domain.comment.service.CommentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@Tag(name = "댓글 API", description = "댓글 관련 API")
@RestController
@RequestMapping("/api/comments")
class CommentController(
    private val commentService: CommentService,
) {
    @Operation(summary = "댓글 목록 조회 API")
    @GetMapping
    fun getComments(
        @Parameter(description = "게시글 ID", example = "1")
        @RequestParam postId: Long,
        @Parameter(description = "페이지 번호 (1부터 시작)", example = "1")
        @RequestParam(defaultValue = "1") page: Int,
        @Parameter(description = "페이지 크기", example = "20")
        @RequestParam(defaultValue = "20") pageSize: Int,
    ): ResponseEntity<ApiResponse<Map<String, Any>>> {
        val comments: Page<CommentDto> = commentService.getComments(postId, page, pageSize)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = Const.RESULT_MESSAGE_SUCCESS,
                data = mapOf(
                    "data" to comments.content,
                    "meta" to mapOf(
                        "pagination" to mapOf(
                            "page" to comments.number + 1,
                            "pageSize" to comments.size,
                            "pageCount" to comments.totalPages,
                            "total" to comments.totalElements
                        )
                    )
                )
            )
        )
    }

    @Operation(summary = "댓글 생성 API")
    @PostMapping
    fun createComment(
        @Valid @RequestBody request: CreateCommentRequestDto,
        @AuthenticationPrincipal userDetails: UserDetails,
    ): ResponseEntity<ApiResponse<CommentDto>> {
        val userId = userDetails.username.toLong()
        val comment = commentService.createComment(userId, request)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = "댓글이 생성되었습니다",
                data = comment
            )
        )
    }

    @Operation(summary = "댓글 수정 API")
    @PutMapping("/{documentId}")
    fun updateComment(
        @Parameter(description = "댓글 ID", example = "1")
        @PathVariable documentId: Long,
        @Valid @RequestBody request: UpdateCommentRequestDto,
        @AuthenticationPrincipal userDetails: UserDetails,
    ): ResponseEntity<ApiResponse<CommentDto>> {
        val userId = userDetails.username.toLong()
        val comment = commentService.updateComment(documentId, userId, request)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = "댓글이 수정되었습니다",
                data = comment
            )
        )
    }

    @Operation(summary = "댓글 삭제 API")
    @DeleteMapping("/{documentId}")
    fun deleteComment(
        @Parameter(description = "댓글 ID", example = "1")
        @PathVariable documentId: Long,
        @AuthenticationPrincipal userDetails: UserDetails,
    ): ResponseEntity<ApiResponse<Unit>> {
        val userId = userDetails.username.toLong()
        commentService.deleteComment(documentId, userId)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = "댓글이 삭제되었습니다"
            )
        )
    }
}

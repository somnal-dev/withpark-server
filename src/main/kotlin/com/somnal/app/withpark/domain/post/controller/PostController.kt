package com.somnal.app.withpark.domain.post.controller

import com.somnal.app.withpark.common.ApiResponse
import com.somnal.app.withpark.common.Const
import com.somnal.app.withpark.domain.post.dto.CreatePostRequestDto
import com.somnal.app.withpark.domain.post.dto.PostDto
import com.somnal.app.withpark.domain.post.dto.PostLikeResponseDto
import com.somnal.app.withpark.domain.post.dto.UpdatePostRequestDto
import com.somnal.app.withpark.domain.post.service.PostService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@Tag(name = "게시글 API", description = "게시글 관련 API")
@RestController
@RequestMapping("/api/posts")
class PostController(
    private val postService: PostService,
) {
    @Operation(summary = "게시글 목록 조회 API")
    @GetMapping
    fun getPosts(
        @Parameter(description = "페이지 번호 (1부터 시작)", example = "1")
        @RequestParam(defaultValue = "1") page: Int,
        @Parameter(description = "페이지 크기", example = "10")
        @RequestParam(defaultValue = "10") pageSize: Int,
        @Parameter(description = "정렬 기준 (createdAt:desc, viewCount:desc)", example = "createdAt:desc")
        @RequestParam(required = false) sort: String?,
        @Parameter(description = "검색 키워드", example = "골프")
        @RequestParam(required = false) search: String?,
        @AuthenticationPrincipal userDetails: UserDetails?,
    ): ResponseEntity<ApiResponse<Map<String, Any>>> {
        val userId = userDetails?.username?.toLongOrNull()
        val posts: Page<PostDto> = postService.getPosts(page, pageSize, sort, search, userId)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = Const.RESULT_MESSAGE_SUCCESS,
                data = mapOf(
                    "data" to posts.content,
                    "meta" to mapOf(
                        "pagination" to mapOf(
                            "page" to posts.number + 1,
                            "pageSize" to posts.size,
                            "pageCount" to posts.totalPages,
                            "total" to posts.totalElements
                        )
                    )
                )
            )
        )
    }

    @Operation(summary = "게시글 상세 조회 API")
    @GetMapping("/{documentId}")
    fun getPost(
        @Parameter(description = "게시글 ID", example = "1")
        @PathVariable documentId: Long,
        @AuthenticationPrincipal userDetails: UserDetails?,
    ): ResponseEntity<ApiResponse<PostDto>> {
        val userId = userDetails?.username?.toLongOrNull()
        val post = postService.getPost(documentId, userId)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = Const.RESULT_MESSAGE_SUCCESS,
                data = post
            )
        )
    }

    @Operation(summary = "게시글 생성 API")
    @PostMapping
    fun createPost(
        @Valid @RequestBody request: CreatePostRequestDto,
        @AuthenticationPrincipal userDetails: UserDetails,
    ): ResponseEntity<ApiResponse<PostDto>> {
        val userId = userDetails.username.toLong()
        val post = postService.createPost(userId, request)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = "게시글이 생성되었습니다",
                data = post
            )
        )
    }

    @Operation(summary = "게시글 수정 API")
    @PutMapping("/{documentId}")
    fun updatePost(
        @Parameter(description = "게시글 ID", example = "1")
        @PathVariable documentId: Long,
        @Valid @RequestBody request: UpdatePostRequestDto,
        @AuthenticationPrincipal userDetails: UserDetails,
    ): ResponseEntity<ApiResponse<PostDto>> {
        val userId = userDetails.username.toLong()
        val post = postService.updatePost(documentId, userId, request)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = "게시글이 수정되었습니다",
                data = post
            )
        )
    }

    @Operation(summary = "게시글 삭제 API")
    @DeleteMapping("/{documentId}")
    fun deletePost(
        @Parameter(description = "게시글 ID", example = "1")
        @PathVariable documentId: Long,
        @AuthenticationPrincipal userDetails: UserDetails,
    ): ResponseEntity<ApiResponse<Unit>> {
        val userId = userDetails.username.toLong()
        postService.deletePost(documentId, userId)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = "게시글이 삭제되었습니다"
            )
        )
    }

    @Operation(summary = "게시글 좋아요 토글 API")
    @PostMapping("/{postId}/like")
    fun toggleLike(
        @Parameter(description = "게시글 ID", example = "1")
        @PathVariable postId: Long,
        @AuthenticationPrincipal userDetails: UserDetails,
    ): ResponseEntity<ApiResponse<PostLikeResponseDto>> {
        val userId = userDetails.username.toLong()
        val response = postService.toggleLike(postId, userId)

        return ResponseEntity.ok(
            ApiResponse.success(
                message = Const.RESULT_MESSAGE_SUCCESS,
                data = response
            )
        )
    }
}

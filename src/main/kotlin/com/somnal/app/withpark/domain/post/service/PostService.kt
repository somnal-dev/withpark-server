package com.somnal.app.withpark.domain.post.service

import com.somnal.app.withpark.domain.comment.repository.CommentRepository
import com.somnal.app.withpark.domain.post.dto.*
import com.somnal.app.withpark.domain.post.entity.Post
import com.somnal.app.withpark.domain.post.entity.PostLike
import com.somnal.app.withpark.domain.post.repository.PostImageRepository
import com.somnal.app.withpark.domain.post.repository.PostLikeRepository
import com.somnal.app.withpark.domain.post.repository.PostRepository
import com.somnal.app.withpark.domain.user.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostService(
    private val postRepository: PostRepository,
    private val postImageRepository: PostImageRepository,
    private val postLikeRepository: PostLikeRepository,
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
) {
    fun getPosts(
        page: Int,
        pageSize: Int,
        sort: String?,
        search: String?,
        currentUserId: Long?,
    ): Page<PostDto> {
        val pageable: Pageable = PageRequest.of(page - 1, pageSize)

        val posts = when {
            !search.isNullOrBlank() -> postRepository.searchByTitle(search, pageable)
            sort == "viewCount:desc" -> postRepository.findAllOrderByViewCount(pageable)
            else -> postRepository.findAllWithUser(pageable)
        }

        return posts.map { post ->
            val images = postImageRepository.findByPostId(post.id).map { image ->
                ImageDto(
                    id = image.id,
                    url = image.url,
                    name = image.name,
                    size = image.size,
                    width = image.width,
                    height = image.height,
                    mimeType = image.mimeType
                )
            }
            val isLiked = currentUserId?.let { postLikeRepository.existsByPostIdAndUserId(post.id, it) } ?: false
            val actualCommentCount = commentRepository.countByPostId(post.id)
            PostDto.fromEntity(post, images, isLiked, actualCommentCount)
        }
    }

    @Transactional
    fun getPost(postId: Long, currentUserId: Long?): PostDto {
        val post = postRepository.findByIdWithUser(postId)
            ?: throw IllegalArgumentException("게시글을 찾을 수 없습니다")

        // 조회수 증가
        post.viewCount++
        postRepository.save(post)

        val images = postImageRepository.findByPostId(post.id).map { image ->
            ImageDto(
                id = image.id,
                url = image.url,
                name = image.name,
                size = image.size,
                width = image.width,
                height = image.height,
                mimeType = image.mimeType
            )
        }

        val isLiked = currentUserId?.let { postLikeRepository.existsByPostIdAndUserId(post.id, it) } ?: false
        val actualCommentCount = commentRepository.countByPostId(post.id)

        return PostDto.fromEntity(post, images, isLiked, actualCommentCount)
    }

    @Transactional
    fun createPost(userId: Long, request: CreatePostRequestDto): PostDto {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("사용자를 찾을 수 없습니다") }

        val post = Post(
            title = request.title,
            content = request.content,
            user = user
        )

        val savedPost = postRepository.save(post)

        return PostDto.fromEntity(savedPost)
    }

    @Transactional
    fun updatePost(postId: Long, userId: Long, request: UpdatePostRequestDto): PostDto {
        val post = postRepository.findByIdWithUser(postId)
            ?: throw IllegalArgumentException("게시글을 찾을 수 없습니다")

        if (post.user.id != userId) {
            throw IllegalArgumentException("게시글 수정 권한이 없습니다")
        }

        request.title?.let { post.title = it }
        request.content?.let { post.content = it }

        val updatedPost = postRepository.save(post)

        val images = postImageRepository.findByPostId(updatedPost.id).map { image ->
            ImageDto(
                id = image.id,
                url = image.url,
                name = image.name,
                size = image.size,
                width = image.width,
                height = image.height,
                mimeType = image.mimeType
            )
        }

        val isLiked = postLikeRepository.existsByPostIdAndUserId(updatedPost.id, userId)
        val actualCommentCount = commentRepository.countByPostId(updatedPost.id)

        return PostDto.fromEntity(updatedPost, images, isLiked, actualCommentCount)
    }

    @Transactional
    fun deletePost(postId: Long, userId: Long) {
        val post = postRepository.findById(postId)
            .orElseThrow { IllegalArgumentException("게시글을 찾을 수 없습니다") }

        if (post.user.id != userId) {
            throw IllegalArgumentException("게시글 삭제 권한이 없습니다")
        }

        postImageRepository.deleteByPostId(postId)
        postRepository.delete(post)
    }

    @Transactional
    fun toggleLike(postId: Long, userId: Long): PostLikeResponseDto {
        val post = postRepository.findById(postId)
            .orElseThrow { IllegalArgumentException("게시글을 찾을 수 없습니다") }

        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("사용자를 찾을 수 없습니다") }

        val existingLike = postLikeRepository.findByPostIdAndUserId(postId, userId)

        return if (existingLike != null) {
            // Unlike
            postLikeRepository.delete(existingLike)
            post.likeCount = maxOf(0, post.likeCount - 1)
            postRepository.save(post)

            PostLikeResponseDto(
                postId = postId,
                action = "unlike",
                likeCount = post.likeCount,
                isLiked = false
            )
        } else {
            // Like
            val like = PostLike(post = post, user = user)
            postLikeRepository.save(like)
            post.likeCount++
            postRepository.save(post)

            PostLikeResponseDto(
                postId = postId,
                action = "like",
                likeCount = post.likeCount,
                isLiked = true
            )
        }
    }
}

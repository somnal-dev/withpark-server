package com.somnal.app.withpark.domain.place.dto

import com.somnal.app.withpark.domain.place.entity.Place
import com.somnal.app.withpark.domain.post.dto.ImageDto
import com.somnal.app.withpark.domain.user.dto.UserDto
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "골프장 정보 DTO")
data class PlaceDto(
    val id: Long,
    val documentId: String,
    val name: String,
    val area: String,
    val address: String?,
    val size: String?,
    val holeCount: String?,
    val longitude: String?,
    val latitude: String?,
    val author: UserDto?,
    val images: List<ImageDto> = emptyList(),
    val likeCount: Long,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
) {
    companion object {
        fun fromEntity(place: Place, images: List<ImageDto> = emptyList()): PlaceDto =
            PlaceDto(
                id = place.id,
                documentId = place.id.toString(),
                name = place.name,
                area = place.area,
                address = place.address,
                size = place.size,
                holeCount = place.holeCount,
                longitude = place.longitude,
                latitude = place.latitude,
                author = place.author?.let { UserDto.fromEntity(it) },
                images = images,
                likeCount = place.likeCount,
                createdAt = place.createdAt,
                updatedAt = place.updatedAt
            )
    }
}

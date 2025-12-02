package com.somnal.app.withpark.domain.place.service

import com.somnal.app.withpark.domain.place.dto.PlaceDto
import com.somnal.app.withpark.domain.place.repository.PlaceImageRepository
import com.somnal.app.withpark.domain.place.repository.PlaceRepository
import com.somnal.app.withpark.domain.post.dto.ImageDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PlaceService(
    private val placeRepository: PlaceRepository,
    private val placeImageRepository: PlaceImageRepository,
) {
    fun getPlaces(
        page: Int,
        pageSize: Int,
        sort: String?,
        area: String?,
        search: String?,
    ): Page<PlaceDto> {
        val pageable: Pageable = PageRequest.of(page - 1, pageSize)

        val places = when {
            !search.isNullOrBlank() -> placeRepository.searchByName(search, pageable)
            !area.isNullOrBlank() -> placeRepository.findByArea(area, pageable)
            sort == "likeCount:desc" -> placeRepository.findAllOrderByLikeCount(pageable)
            else -> placeRepository.findAllOrderByCreatedAt(pageable)
        }

        return places.map { place ->
            val images = placeImageRepository.findByPlaceId(place.id).map { image ->
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
            PlaceDto.fromEntity(place, images)
        }
    }

    fun getPlace(placeId: Long): PlaceDto {
        val place = placeRepository.findById(placeId)
            .orElseThrow { IllegalArgumentException("골프장을 찾을 수 없습니다") }

        val images = placeImageRepository.findByPlaceId(place.id).map { image ->
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

        return PlaceDto.fromEntity(place, images)
    }
}

package com.somnal.app.withpark.domain.place.repository

import com.somnal.app.withpark.domain.place.entity.PlaceImage
import org.springframework.data.jpa.repository.JpaRepository

interface PlaceImageRepository : JpaRepository<PlaceImage, Long> {
    fun findByPlaceId(placeId: Long): List<PlaceImage>
}

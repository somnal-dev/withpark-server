package com.somnal.app.withpark.domain.place.repository

import com.somnal.app.withpark.domain.place.entity.Place
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PlaceRepository : JpaRepository<Place, Long> {
    @Query("SELECT p FROM Place p ORDER BY p.createdAt DESC")
    fun findAllOrderByCreatedAt(pageable: Pageable): Page<Place>

    @Query("SELECT p FROM Place p WHERE p.area = :area ORDER BY p.createdAt DESC")
    fun findByArea(area: String, pageable: Pageable): Page<Place>

    @Query("SELECT p FROM Place p WHERE p.name LIKE %:keyword% ORDER BY p.createdAt DESC")
    fun searchByName(keyword: String, pageable: Pageable): Page<Place>

    @Query("SELECT p FROM Place p ORDER BY p.likeCount DESC")
    fun findAllOrderByLikeCount(pageable: Pageable): Page<Place>
}

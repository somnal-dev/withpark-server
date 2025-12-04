package com.somnal.app.withpark.domain.place.entity

import com.somnal.app.withpark.common.BaseEntity
import com.somnal.app.withpark.domain.user.entity.User
import jakarta.persistence.*

@Entity
@Table(name = "places")
class Place(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var name: String,

    var area: String,

    var address: String? = null,

    var size: String? = null,

    var holeCount: String? = null,

    var longitude: String? = null,

    var latitude: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    val author: User? = null,

    var likeCount: Long = 0,
) : BaseEntity()

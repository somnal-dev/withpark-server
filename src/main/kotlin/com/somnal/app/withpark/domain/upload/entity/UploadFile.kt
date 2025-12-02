package com.somnal.app.withpark.domain.upload.entity

import com.somnal.app.withpark.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "upload_files")
data class UploadFile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val name: String,

    val url: String,

    val size: Long,

    val width: Int? = null,

    val height: Int? = null,

    val mimeType: String,

    val hash: String,

    val ext: String,
) : BaseEntity()

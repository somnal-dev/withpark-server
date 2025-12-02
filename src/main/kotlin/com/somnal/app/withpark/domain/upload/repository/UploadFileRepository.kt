package com.somnal.app.withpark.domain.upload.repository

import com.somnal.app.withpark.domain.upload.entity.UploadFile
import org.springframework.data.jpa.repository.JpaRepository

interface UploadFileRepository : JpaRepository<UploadFile, Long>

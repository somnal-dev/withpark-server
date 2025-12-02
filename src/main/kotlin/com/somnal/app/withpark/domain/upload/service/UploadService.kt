package com.somnal.app.withpark.domain.upload.service

import com.somnal.app.withpark.domain.upload.dto.UploadResponseDto
import com.somnal.app.withpark.domain.upload.entity.UploadFile
import com.somnal.app.withpark.domain.upload.repository.UploadFileRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.security.MessageDigest
import java.util.*
import javax.imageio.ImageIO

@Service
@Transactional
class UploadService(
    private val uploadFileRepository: UploadFileRepository,
    @Value("\${upload.path:uploads}") private val uploadPath: String,
    @Value("\${upload.url:http://localhost:8080/uploads}") private val uploadUrl: String,
) {
    fun uploadFiles(files: List<MultipartFile>): List<UploadResponseDto> {
        // 업로드 디렉토리 생성
        val uploadDir = File(uploadPath)
        if (!uploadDir.exists()) {
            uploadDir.mkdirs()
        }

        return files.map { file ->
            uploadFile(file)
        }
    }

    private fun uploadFile(file: MultipartFile): UploadResponseDto {
        val originalFilename = file.originalFilename ?: "unknown"
        val extension = originalFilename.substringAfterLast(".", "")
        val hash = generateHash(file.bytes)
        val fileName = "${hash}.${extension}"
        val filePath = Paths.get(uploadPath, fileName)

        // 파일 저장
        Files.write(filePath, file.bytes)

        // 이미지인 경우 크기 정보 추출
        var width: Int? = null
        var height: Int? = null

        if (file.contentType?.startsWith("image/") == true) {
            try {
                val image: BufferedImage = ImageIO.read(filePath.toFile())
                width = image.width
                height = image.height
            } catch (e: Exception) {
                // 이미지 파일이지만 읽기 실패한 경우 무시
            }
        }

        val uploadFile = UploadFile(
            name = originalFilename,
            url = "$uploadUrl/$fileName",
            size = file.size,
            width = width,
            height = height,
            mimeType = file.contentType ?: "application/octet-stream",
            hash = hash,
            ext = ".$extension"
        )

        val savedFile = uploadFileRepository.save(uploadFile)

        return UploadResponseDto(
            id = savedFile.id,
            name = savedFile.name,
            url = savedFile.url,
            size = savedFile.size,
            width = savedFile.width,
            height = savedFile.height,
            mime = savedFile.mimeType,
            hash = savedFile.hash,
            ext = savedFile.ext
        )
    }

    private fun generateHash(bytes: ByteArray): String {
        val md = MessageDigest.getInstance("SHA-256")
        val hashBytes = md.digest(bytes)
        return hashBytes.joinToString("") { "%02x".format(it) }.substring(0, 16)
    }
}

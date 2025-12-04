package com.somnal.app.withpark.common

import io.swagger.v3.oas.annotations.Hidden
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Hidden
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ApiResponse<Unit>> {

        logger.debug(e.message ?: "잘못된 요청입니다")

        return ResponseEntity.badRequest().body(
            ApiResponse.error(e.message ?: "잘못된 요청입니다"),
        )
    }


    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(e: IllegalStateException): ResponseEntity<ApiResponse<Unit>> {

        logger.debug(e.message ?: "요청을 처리할 수 없습니다")

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ApiResponse.error(e.message ?: "요청을 처리할 수 없습니다"),
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Unit>> {
        val errors =
            e.bindingResult.allErrors.joinToString(", ") { error ->
                when (error) {
                    is FieldError -> "${error.field}: ${error.defaultMessage}"
                    else -> error.defaultMessage ?: "검증 오류"
                }
            }

        logger.debug(errors)

        return ResponseEntity.badRequest().body(
            ApiResponse.error("입력값 검증 실패: $errors"),
        )
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(e: NoSuchElementException): ResponseEntity<ApiResponse<Unit>> {

        logger.debug(e.message ?: "요청한 리소스를 찾을 수 없습니다")

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ApiResponse.error(e.message ?: "요청한 리소스를 찾을 수 없습니다"),
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(e: Exception): ResponseEntity<ApiResponse<Unit>> {

        logger.error("서버 내부 오류가 발생했습니다: ${e.message}", e)

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ApiResponse.error("서버 내부 오류가 발생했습니다"),
        )
    }
}

package com.commerce.kotlin.common.handler

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(e: NotFoundException): ResponseEntity<ErrorResponse> {
        val notFoundErrorResponse = ErrorResponse.create(e, HttpStatus.NOT_FOUND, "Not Found")
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundErrorResponse)
    }
}
package com.commerce.kotlin.domain.file

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RequestMapping("files")
@RestController
class FileController(
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun upload(@RequestParam("file") file: MultipartFile) {

    }
}
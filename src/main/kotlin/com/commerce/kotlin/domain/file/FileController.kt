package com.commerce.kotlin.domain.file

import com.commerce.kotlin.domain.file.dto.CreateFileResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.UUID

@RequestMapping("files")
@RestController
class FileController(
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun upload(@RequestParam("file") file: MultipartFile): CreateFileResponse {
        val ext = file.originalFilename?.split(".")?.last()
        val fileName = UUID.randomUUID().toString() + ".${ext}"
        val filePath = "/Users/kapri.kim/Desktop/${fileName}"
        if(!file.isEmpty) {
            file.transferTo(File(filePath))
        }
        return CreateFileResponse(filePath)
    }
}
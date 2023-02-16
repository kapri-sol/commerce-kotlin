package com.commerce.kotlin.common.entity

import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@MappedSuperclass
abstract class BaseEntity {
    @CreatedDate
    open val createdAt: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    open val updateAt: LocalDateTime = LocalDateTime.now()
}
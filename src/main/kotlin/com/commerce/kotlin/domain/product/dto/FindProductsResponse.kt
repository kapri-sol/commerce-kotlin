package com.commerce.kotlin.domain.product.dto

data class FindProductsResponse(
    val content: Collection<FindProductResponse>,
    val totalPage: Int,
    val size: Int
)

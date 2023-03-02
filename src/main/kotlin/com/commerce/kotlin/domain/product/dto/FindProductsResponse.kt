package com.commerce.kotlin.domain.product.dto

data class FindProductsResponse(
    val data: Collection<FindProductResponse>,
    val count: Int
)

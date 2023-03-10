package com.commerce.kotlin.domain.product

import com.commerce.kotlin.common.constant.SESSION_NAME
import com.commerce.kotlin.common.constant.SessionBody
import com.commerce.kotlin.domain.product.dto.*
import com.commerce.kotlin.security.authentication.CustomUserDetails
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttribute

@RequestMapping("products")
@RestController
class ProductController(
    private val productService: ProductService
) {
    @GetMapping
    fun findProducts(): FindProductsResponse {
        val products = productService.findAllProducts()

        return FindProductsResponse(
            data = products.map { FindProductResponse(
                id = it.id!!,
                title = it.title,
                description = it.description,
                image= it.image,
                price = it.price,
                stockQuantity = it.stockQuantity
            ) },
            count = products.size
        )
    }

    @GetMapping("{id}")
    fun getProduct(@PathVariable("id") productId: Long): FindProductResponse {
        val product = productService.findProductById(productId)

        return FindProductResponse(
            id = product.id!!,
            title = product.title,
            description = product.description,
            image= product.image,
            price = product.price,
            stockQuantity = product.stockQuantity
        )
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postProduct(
        authentication: Authentication,
        @RequestBody createProductDto: CreateProductDto
    ): CreateProductResponse {
        val sellerId = (authentication.principal as CustomUserDetails).sellerId ?: throw NotFoundException()
        val productId = productService.createProduct(sellerId, createProductDto)

        return CreateProductResponse(
            productId = productId
        )
    }

    @PatchMapping("{id}")
    fun patchProduct(
        @SessionAttribute(name = SESSION_NAME) sessionBody: SessionBody,
        @PathVariable("id") productId: Long,
        @RequestBody updateProductDto: UpdateProductDto
    ) {
        val sellerId = sessionBody.sellerId ?: throw IllegalAccessException()
        this.productService.updateProduct(productId, sellerId, updateProductDto)
    }
}
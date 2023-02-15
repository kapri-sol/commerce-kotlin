package com.commerce.kotlin.domain.product

import com.commerce.kotlin.common.constant.SESSION_NAME
import com.commerce.kotlin.common.constant.SessionBody
import com.commerce.kotlin.domain.product.dto.CreateProductDto
import com.commerce.kotlin.domain.product.dto.GetProductResponse
import com.commerce.kotlin.domain.product.dto.PostProductResponse
import com.commerce.kotlin.domain.product.dto.UpdateProductDto
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
    @GetMapping("{id}")
    fun getProduct(@PathVariable("id") productId: Long): GetProductResponse {
        val product = productService.findProductById(productId)

        return GetProductResponse(
            name = product.name,
            description = product.description,
            price = product.price,
            stockQuantity = product.stockQuantity
        )
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postProduct(
        authentication: Authentication,
        @RequestBody createProductDto: CreateProductDto
    ): PostProductResponse {
        val sellerId = (authentication.principal as CustomUserDetails).sellerId ?: throw NotFoundException()
        val productId = productService.createProduct(sellerId, createProductDto)

        return PostProductResponse(
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
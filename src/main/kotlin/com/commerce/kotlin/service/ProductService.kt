package com.commerce.kotlin.service

import com.commerce.kotlin.dto.CreateProductDto
import com.commerce.kotlin.dto.UpdateProductDto
import com.commerce.kotlin.entity.Product
import com.commerce.kotlin.repository.ProductRepository
import com.commerce.kotlin.repository.SellerRepository
import jakarta.transaction.Transactional
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Transactional
@Service
class ProductService(
    private val sellerRepository: SellerRepository,
    private val productRepository: ProductRepository
) {

    fun createProduct(sellerId: Long, createProductDto: CreateProductDto): Long {
        val seller = this.sellerRepository.findByIdOrNull(sellerId) ?: throw NotFoundException();
        val product = Product(
            name = createProductDto.name,
            description = createProductDto.description,
            price = createProductDto.price,
            stockQuantity = createProductDto.stockQuantity
        )
        product.setSeller(seller);
        return this.productRepository.save(product).id!!
    }

    fun findProduct(productId: Long): Product {
        return this.productRepository.findByIdOrNull(productId) ?: throw NotFoundException()
    }

    fun updateProduct(productId: Long, updateProductDto: UpdateProductDto) {
        val product = this.productRepository.findByIdOrNull(productId) ?: throw NotFoundException();
        product.changeName(updateProductDto.name);
        product.changeDescription(updateProductDto.description);
        product.increaseQuantity(updateProductDto.increaseQuantityCount!!);
    }

    fun removeProduct(productId: Long) {
        val product = this.productRepository.findByIdOrNull(productId) ?: throw NotFoundException();
        product.remove();
    }
}
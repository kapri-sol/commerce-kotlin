package com.commerce.kotlin.domain.product

import com.commerce.kotlin.domain.product.dto.CreateProductDto
import com.commerce.kotlin.domain.product.dto.UpdateProductDto
import com.commerce.kotlin.domain.seller.SellerRepository
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
        val seller = this.sellerRepository.findByIdOrNull(sellerId) ?: throw NotFoundException()
        val product = Product(
            name = createProductDto.name,
            description = createProductDto.description,
            price = createProductDto.price,
            stockQuantity = createProductDto.stockQuantity,
            image = createProductDto.image
        )
        product.connectSeller(seller)
        return this.productRepository.save(product).id!!
    }

    fun findProductById(productId: Long): Product {
        return this.productRepository.findByIdOrNull(productId) ?: throw NotFoundException()
    }

    fun findAllProducts(): MutableList<Product> {
        return this.productRepository.findAll()
    }

    fun updateProduct(productId: Long, sellerId: Long, updateProductDto: UpdateProductDto) {
        val product = this.productRepository.findByIdWithSeller(productId) ?: throw NotFoundException()

        if (product.seller?.id != sellerId) {
            throw IllegalAccessException()
        }

        product.changeName(updateProductDto.name)
        product.changeDescription(updateProductDto.description)
        product.increaseQuantity(updateProductDto.increaseQuantityCount!!)
    }

    fun removeProduct(productId: Long) {
        val product = this.productRepository.findByIdOrNull(productId) ?: throw NotFoundException()
        product.remove()
    }
}
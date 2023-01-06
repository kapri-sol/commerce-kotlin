package com.commerce.kotlin.product

import com.commerce.kotlin.domain.product.dto.CreateProductDto
import com.commerce.kotlin.domain.product.dto.UpdateProductDto
import com.commerce.kotlin.domain.product.Product
import com.commerce.kotlin.domain.seller.Seller
import com.commerce.kotlin.domain.product.ProductService
import com.commerce.kotlin.domain.product.ProductRepository
import com.commerce.kotlin.domain.seller.SellerRepository
import net.datafaker.Faker
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class ProductServiceTest(
    @Autowired val sellerRepository: SellerRepository,
    @Autowired val productRepository: ProductRepository,
    @Autowired val productService: ProductService
) {
    val faker = Faker()

    fun generateSeller(): Seller {
        val seller = Seller(
            name = faker.name().fullName(),
            address = faker.address().fullAddress()
        )
        return this.sellerRepository.save(seller)
    }

    fun generateProduct(): Product {
        val product = Product(
            name = faker.commerce().productName(),
            description = faker.lorem().sentence(),
            price = faker.commerce().price().toDouble(),
            stockQuantity = faker.random().nextInt(1, 1000)
        )
        return this.productRepository.save(product)
    }

    @Test
    fun createProduct() {
        // given
        val seller = generateSeller()
        val createProductDto = CreateProductDto(
            name = faker.commerce().productName(),
            description = faker.lorem().sentence(),
            price = faker.commerce().price().toDouble(),
            stockQuantity = faker.random().nextInt(1, 1000)
        )
        // when
        val productId = this.productService.createProduct(seller.id!!, createProductDto)
        val product = this.productRepository.findByIdOrNull(productId)

        // then
        assertThat(product?.id).isEqualTo(productId)
        assertThat(product?.name).isEqualTo(createProductDto.name)
        assertThat(product?.description).isEqualTo(createProductDto.description)
        assertThat(product?.price).isEqualTo(createProductDto.price)
        assertThat(product?.stockQuantity).isEqualTo(createProductDto.stockQuantity)
    }

    @Test
    fun findProduct() {
        // given
        val product = this.generateProduct()
        //when
        val findProduct = this.productService.findProduct(product.id!!)
        //then
        assertThat(product.id).isEqualTo(findProduct.id)
        assertThat(product.name).isEqualTo(findProduct.name)
        assertThat(product.description).isEqualTo(findProduct.description)
        assertThat(product.price).isEqualTo(findProduct.price)
        assertThat(product.stockQuantity).isEqualTo(findProduct.stockQuantity)
    }

    @Test
    fun updateProduct() {
        // given
        val product = generateProduct()
        val updateProductDto = UpdateProductDto(
            name = faker.commerce().productName(),
            description = faker.lorem().sentence(),
            increaseQuantityCount = faker.random().nextInt(1, 10)
        )
        // when
        productService.updateProduct(
            productId = product.id!!,
            updateProductDto = updateProductDto
        )
        val updateProduct = this.productRepository.findByIdOrNull(product.id)
        // then
        assertThat(updateProduct?.id).isEqualTo(product.id)
        assertThat(updateProduct?.name).isEqualTo(updateProductDto.name)
        assertThat(updateProduct?.description).isEqualTo(updateProductDto.description)
        assertThat(updateProduct?.stockQuantity).isEqualTo(product.stockQuantity + updateProductDto.increaseQuantityCount!!)
    }

    @Test
    fun removeProduct() {
        // given
        val product = generateProduct()
        // when
        this.productService.removeProduct(productId = product.id!!)
        val removeProduct = this.productRepository.findByIdOrNull(product.id)
        // then
        assertThat(removeProduct).isNull()
    }
}
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
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class ProductServiceTest{
    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var productService: ProductService

    private companion object {
        lateinit var initialSeller: Seller
        val initialProducts: ArrayList<Product> = arrayListOf()

        val faker = Faker()

        @JvmStatic
        @BeforeAll
        fun init(@Autowired sellerRepository: SellerRepository, @Autowired productRepository: ProductRepository): Unit {
            initialSeller = sellerRepository.save(
                Seller(
                    name = faker.name().fullName(),
                    address = faker.address().fullAddress()
                )
            )

            for (i: Int in 1..3) {
                initialProducts.add(
                    productRepository.save(
                        Product(
                            title = faker.commerce().productName(),
                            description = faker.lorem().sentence(),
                            price = faker.commerce().price().toDouble().toInt(),
                            image = "/",
                            stockQuantity = faker.random().nextInt(1, 1000)
                        )
                    )
                )
            }
        }
    }

    @Test
    @DisplayName("상품을 생성한다.")
    fun createProduct() {
        // given
        val seller = initialSeller

        val createProductDto = CreateProductDto(
            name = faker.commerce().productName(),
            description = faker.lorem().sentence(),
            price = faker.commerce().price().toDouble().toInt(),
            image = "/",
            stockQuantity = faker.random().nextInt(1, 1000)
        )
        // when
        val productId = this.productService.createProduct(seller.id!!, createProductDto)
        val product = this.productRepository.findByIdOrNull(productId)

        // then
        assertThat(product?.id).isEqualTo(productId)
        assertThat(product?.title).isEqualTo(createProductDto.name)
        assertThat(product?.description).isEqualTo(createProductDto.description)
        assertThat(product?.price).isEqualTo(createProductDto.price)
        assertThat(product?.stockQuantity).isEqualTo(createProductDto.stockQuantity)
    }

    @Test
    @DisplayName("상품을 검색한다.")
    fun findProduct() {
        // given
        val product = initialProducts[0]

        //when
        val findProduct = this.productService.findProductById(product.id!!)
        //then
        assertThat(product.id).isEqualTo(findProduct.id)
        assertThat(product.title).isEqualTo(findProduct.title)
        assertThat(product.description).isEqualTo(findProduct.description)
        assertThat(product.price).isEqualTo(findProduct.price)
        assertThat(product.stockQuantity).isEqualTo(findProduct.stockQuantity)
    }

    @Test
    @DisplayName("상품의 판매자가 상품 정보를 수정한다.")
    fun updateProduct() {
        // given
        val seller = initialSeller
        val product = initialProducts[0]
        product.connectSeller(seller)
        productRepository.saveAndFlush(product)

        val updateProductDto = UpdateProductDto(
            name = faker.commerce().productName(),
            description = faker.lorem().sentence(),
            increaseQuantityCount = faker.random().nextInt(1, 10)
        )

        // when
        productService.updateProduct(
            productId = product.id!!,
            sellerId = seller.id!!,
            updateProductDto = updateProductDto
        )
        val updateProduct = this.productRepository.findByIdOrNull(product.id)

        // then
        assertThat(updateProduct?.id).isEqualTo(product.id)
        assertThat(updateProduct?.title).isEqualTo(updateProductDto.name)
        assertThat(updateProduct?.description).isEqualTo(updateProductDto.description)
        assertThat(updateProduct?.stockQuantity).isEqualTo(product.stockQuantity + updateProductDto.increaseQuantityCount!!)
    }

    @Test
    @DisplayName("상품을 제거한다.")
    fun removeProduct() {
        // given
        val product = initialProducts[0]
        // when
        this.productService.removeProduct(productId = product.id!!)
        val removeProduct = this.productRepository.findByIdOrNull(product.id)
        // then
        assertThat(removeProduct?.deleted).isTrue
    }
}
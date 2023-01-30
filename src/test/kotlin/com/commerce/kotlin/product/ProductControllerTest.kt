package com.commerce.kotlin.product

import com.commerce.kotlin.domain.account.Account
import com.commerce.kotlin.domain.account.AccountRepository
import com.commerce.kotlin.domain.product.Product
import com.commerce.kotlin.domain.product.ProductRepository
import com.commerce.kotlin.domain.product.dto.CreateProductDto
import com.commerce.kotlin.domain.product.dto.GetProductResponse
import com.commerce.kotlin.domain.product.dto.PostProductResponse
import com.commerce.kotlin.domain.seller.Seller
import com.commerce.kotlin.domain.seller.SellerRepository
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import net.datafaker.Faker
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@AutoConfigureMockMvc
@SpringBootTest
class ProductControllerTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val accountRepository: AccountRepository,
    @Autowired val sellerRepository: SellerRepository,
    @Autowired val productRepository: ProductRepository
) {
    val faker = Faker()

    @Test
    @DisplayName("GET Product")
    fun getProduct() {
        // given
        val account = accountRepository.save(
            Account(
                email = faker.internet().emailAddress(),
                phoneNumber = faker.phoneNumber().phoneNumber(),
                password = faker.internet().password()
            )
        )
        val seller = sellerRepository.save(
            Seller(
                name = faker.name().fullName(),
                address = faker.address().fullAddress()
            )
        )
        account.setSeller(seller)

        val product = productRepository.save(
            Product(
                name = faker.commerce().productName(),
                description = faker.lorem().sentence(),
                price = faker.commerce().price().toDouble().toInt(),
                stockQuantity = faker.random().nextInt(1, 100).toInt()
            )
        )

        val getProductResponse = GetProductResponse(
            name = product.name,
            description = product.description,
            price = product.price,
            stockQuantity = product.stockQuantity
        )

        // when
        mockMvc.perform(
            get("/products/${product.id}")
        )
            // then
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(getProductResponse)))
    }

    @Test
    @DisplayName("POST Product")
    fun postProduct() {
        // given
        val account = accountRepository.save(
            Account(
                email = faker.internet().emailAddress(),
                phoneNumber = faker.phoneNumber().phoneNumber(),
                password = faker.internet().password()
            )
        )
        val seller = sellerRepository.save(
            Seller(
                name = faker.name().fullName(),
                address = faker.address().fullAddress()
            )
        )
        account.setSeller(seller)

        val createProductDto = CreateProductDto(
            name = faker.commerce().productName(),
            description = faker.lorem().sentence(),
            price = faker.commerce().price().toDouble().toInt(),
            stockQuantity = faker.random().nextInt(1, 100).toInt()
        )

        //when
        val perform = mockMvc.perform(
            post("/products")
                .content(jacksonObjectMapper().writeValueAsString(createProductDto))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)

        val postProductResponse = jacksonObjectMapper().readValue(
            perform.andReturn().response.contentAsString,
            PostProductResponse::class.java
        )

        Assertions.assertThat(postProductResponse?.productId).isNotNull
    }
}
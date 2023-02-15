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
import com.commerce.kotlin.util.WithMockCustomUser
import com.fasterxml.jackson.databind.ObjectMapper
import net.datafaker.Faker
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@AutoConfigureMockMvc
@SpringBootTest
class ProductControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var sellerRepository: SellerRepository

    @Autowired
    lateinit var productRepository: ProductRepository

    @Autowired
    lateinit var objectMapper: ObjectMapper

    val faker = Faker()

    fun generateSeller(): Seller {
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
        return seller
    }

    @Test
    @DisplayName("GET Product")
    fun getProduct() {
        // given
        generateSeller()

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
            .andExpect(content().json(objectMapper.writeValueAsString(getProductResponse)))
    }

    @Test
    @DisplayName("POST Product")
    @WithMockCustomUser(accountId = 1L, sellerId = 1L)
    fun postProduct() {
        // given
        generateSeller()

        val createProductDto = CreateProductDto(
            name = faker.commerce().productName(),
            description = faker.lorem().sentence(),
            price = faker.commerce().price().toDouble().toInt(),
            stockQuantity = faker.random().nextInt(1, 100).toInt()
        )

        //when
        val perform = mockMvc.perform(
            post("/products")
                .content(objectMapper.writeValueAsString(createProductDto))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)

        val postProductResponse = objectMapper.readValue(
            perform.andReturn().response.contentAsString,
            PostProductResponse::class.java
        )

        Assertions.assertThat(postProductResponse?.productId).isNotNull
    }
}
package com.commerce.kotlin.product

import com.commerce.kotlin.domain.account.Account
import com.commerce.kotlin.domain.account.AccountRepository
import com.commerce.kotlin.domain.product.Product
import com.commerce.kotlin.domain.product.ProductRepository
import com.commerce.kotlin.domain.product.dto.CreateProductDto
import com.commerce.kotlin.domain.product.dto.CreateProductResponse
import com.commerce.kotlin.domain.product.dto.FindProductResponse
import com.commerce.kotlin.domain.seller.Seller
import com.commerce.kotlin.domain.seller.SellerRepository
import com.commerce.kotlin.util.WithMockCustomUser
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.transaction.Transactional
import net.datafaker.Faker
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Transactional
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


    companion object {
        private lateinit var initialAccount: Account
        private lateinit var initialSeller: Seller
        val initialProducts: ArrayList<Product> = arrayListOf()

        val faker = Faker()

        @JvmStatic
        @BeforeAll
        fun init(
            @Autowired accountRepository: AccountRepository,
            @Autowired sellerRepository: SellerRepository,
            @Autowired productRepository: ProductRepository
        ): Unit {
            initialAccount = accountRepository.save(
                Account(
                    email = faker.internet().emailAddress(),
                    phoneNumber = faker.phoneNumber().phoneNumber(),
                    password = faker.internet().password()
                )
            )

            initialSeller = sellerRepository.save(
                Seller(
                    name = faker.name().fullName(),
                    address = faker.address().fullAddress()
                )
            )

            for (i: Int in 1.. 3) {
                initialProducts.add(
                    productRepository.save(
                        Product(
                            name = faker.commerce().productName(),
                            description = faker.lorem().sentence(),
                            price = faker.commerce().price().toDouble().toInt(),
                            stockQuantity = faker.random().nextInt(1, 100).toInt()
                        )
                    )
                )
            }
        }
    }

    @Test
    @DisplayName("GET Product")
    fun getProduct() {
        val product = initialProducts[0]
        // given
        val getProductResponse = FindProductResponse(
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
            .andDo(
                document("product", responseFields(
                        fieldWithPath("name").description("이름"),
                        fieldWithPath("description").description("설명"),
                        fieldWithPath("price").description("가격"),
                        fieldWithPath("stockQuantity").description("수량")
                    )
                )
            )
    }

    @Test
    @DisplayName("POST Product")
    @WithMockCustomUser(accountId = 1L, sellerId = 1L)
    fun postProduct() {
        // given
        val createProductDto = CreateProductDto(
            name = faker.commerce().productName(),
            description = faker.lorem().sentence(),
            price = faker.commerce().price().toDouble().toInt(),
            stockQuantity = faker.random().nextInt(1, 100).toInt()
        )

        val createProductResponse = CreateProductResponse(
            productId = 4L
        )

        //when
        mockMvc.perform(
            post("/products")
                .content(objectMapper.writeValueAsString(createProductDto))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)
            .andExpect(content().json(objectMapper.writeValueAsString(createProductResponse)))
            .andDo(
                document(
                    "product", requestFields(
                        fieldWithPath("name").description("이름"),
                        fieldWithPath("description").description("설명"),
                        fieldWithPath("price").description("가격"),
                        fieldWithPath("stockQuantity").description("수량")
                    )
                )
            )

    }
}
package com.commerce.kotlin.product

import com.commerce.kotlin.domain.product.Product
import com.commerce.kotlin.domain.product.ProductService
import com.commerce.kotlin.domain.product.dto.CreateProductDto
import com.commerce.kotlin.domain.product.dto.CreateProductResponse
import com.commerce.kotlin.domain.product.dto.FindProductResponse
import com.commerce.kotlin.util.WithMockCustomUser
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import net.datafaker.Faker
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
@SpringBootTest
class ProductControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var productService: ProductService

    @Autowired
    lateinit var objectMapper: ObjectMapper

    private val faker = Faker()

    @Test
    @DisplayName("GET Product")
    fun getProduct() {
        // given
        val product = Product(
            title = faker.commerce().productName(),
            description = faker.lorem().sentence(),
            price = faker.commerce().price().toDouble().toInt(),
            stockQuantity = faker.random().nextInt(1, 100).toInt(),
            image = "/"
        )

        val getProductResponse = FindProductResponse(
            title = product.title,
            description = product.description,
            price = product.price,
            stockQuantity = product.stockQuantity
        )

        every { productService.findProductById(1L) } returns product

        // when
        mockMvc.perform(
            get("/products/${1L}")
        )
            // then
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(getProductResponse)))
            .andDo(
                document("product/get-products", responseFields(
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
            stockQuantity = faker.random().nextInt(1, 100).toInt(),
            image = "/"
        )

        val createProductResponse = CreateProductResponse(
            productId = 1L
        )

        every { productService.createProduct(1L, any()) } returns 1L

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
                    "product/post-products", requestFields(
                        fieldWithPath("name").description("이름"),
                        fieldWithPath("description").description("설명"),
                        fieldWithPath("price").description("가격"),
                        fieldWithPath("stockQuantity").description("수량")
                    )
                )
            )

    }
}
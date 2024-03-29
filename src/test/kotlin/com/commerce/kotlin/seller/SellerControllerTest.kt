package com.commerce.kotlin.seller

import com.commerce.kotlin.domain.account.Account
import com.commerce.kotlin.domain.account.AccountRepository
import com.commerce.kotlin.domain.seller.Seller
import com.commerce.kotlin.domain.seller.SellerService
import com.commerce.kotlin.domain.seller.dto.CreateSellerDto
import com.commerce.kotlin.domain.seller.dto.CreateSellerResponse
import com.commerce.kotlin.domain.seller.dto.FindSellerResponse
import com.commerce.kotlin.domain.seller.dto.UpdateSellerDto
import com.commerce.kotlin.util.WithMockCustomUser
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import net.datafaker.Faker
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest()
class SellerControllerTest {
    @MockkBean
    lateinit var sellerService: SellerService

    @MockkBean
    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    private val faker = Faker()

    private val initialSeller = Seller(
        name = faker.name().fullName(),
        address = faker.address().fullAddress()
    )

    @Test
    @WithMockCustomUser(accountId = 2L)
    @DisplayName("POST Seller")
    fun postSeller() {
        // given
        val account = Account(
            email = faker.internet().emailAddress(),
            phoneNumber = faker.phoneNumber().phoneNumber(),
            name = faker.name().fullName(),
            password = faker.internet().password()
        )

        val createSellerDto = CreateSellerDto(
            name = faker.name().fullName(),
            address = faker.address().fullAddress()
        )

        val createSellerResponse = CreateSellerResponse(
            sellerId = 2L
        )

        every { accountRepository.findByIdOrNull(2L) } returns account
        every { sellerService.createSeller(2L, any()) } returns 2L

        // when
        this.mockMvc.perform(
            post("/sellers")
                .content(jacksonObjectMapper().writeValueAsString(createSellerDto))
                .contentType(MediaType.APPLICATION_JSON)
        )
            // then
            .andExpect(status().isCreated)
            .andExpect(content().json(objectMapper.writeValueAsString(createSellerResponse)))
            .andDo(
                MockMvcRestDocumentationWrapper.document(
                    identifier = "seller/post-sellers",
                    resourceDetails = ResourceSnippetParametersBuilder().tag("Seller")
                        .responseFields(
                            fieldWithPath("name").description("이름"),
                            fieldWithPath("address").description("주소"),
                        ).responseFields(
                            fieldWithPath("sellerId").description("판매자 고유번호")

                        )
                )
            )
    }

    @Test
    @WithMockCustomUser(accountId = 1L, sellerId = 1L)
    @DisplayName("GET seller ME")
    fun getSellerMyself() {
        // given
        val getSellerResponse = FindSellerResponse(
            name = initialSeller.name,
            address = initialSeller.address
        )

        every { sellerService.findSellerById(1L) } returns initialSeller

        // when
        mockMvc.perform(
            get("/sellers/me")
        )
            // then
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(getSellerResponse)))
            .andDo(
                MockMvcRestDocumentationWrapper.document(
                    identifier = "seller/get-sellers/me",
                    resourceDetails = ResourceSnippetParametersBuilder().tag("Seller")
                        .responseFields(
                        fieldWithPath("name").description("이름"),
                        fieldWithPath("address").description("주소"),
                    )
                )
            )
    }

    @Test
    @WithMockCustomUser(accountId = 1L, sellerId = 1L)
    @DisplayName("PATCH Seller")
    fun patchSeller() {
        // given
        val updateSellerDto = UpdateSellerDto(
            name = faker.name().username(),
            address = faker.address().fullAddress()
        )

        every { sellerService.updateSeller(1L, any()) } returns Unit

        // when
        this.mockMvc.perform(
            patch("/sellers/me")
                .content(jacksonObjectMapper().writeValueAsString(updateSellerDto))
                .contentType(MediaType.APPLICATION_JSON)
        )
            // then
            .andExpect(status().isNoContent)
            .andDo(
                MockMvcRestDocumentationWrapper.document(
                    identifier = "seller/patch-sellers-me",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .tag("Seller")
                        .requestFields(
                            fieldWithPath("name").description("이름"),
                            fieldWithPath("address").description("주소"),
                        )
                )
            )
    }

    @Test
    @WithMockCustomUser(accountId = 1L, sellerId = 1L)
    @DisplayName("DELETE Seller")
    fun deleteSeller() {
        // given
        every { sellerService.removeSeller(1L) } returns Unit

        // when
        this.mockMvc.perform(
            delete("/sellers/me")
        )
            // then
            .andExpect(status().isNoContent)
            .andDo(
                MockMvcRestDocumentationWrapper.document("seller/delete-sellers/me")
            )
    }
}
package com.commerce.kotlin.seller

import com.commerce.kotlin.common.constant.SESSION_NAME
import com.commerce.kotlin.common.constant.SessionBody
import com.commerce.kotlin.domain.account.Account
import com.commerce.kotlin.domain.account.AccountRepository
import com.commerce.kotlin.domain.seller.Seller
import com.commerce.kotlin.domain.seller.SellerRepository
import com.commerce.kotlin.domain.seller.dto.CreateSellerDto
import com.commerce.kotlin.domain.seller.dto.GetSellerResponse
import com.commerce.kotlin.domain.seller.dto.PostSellerResponse
import com.commerce.kotlin.domain.seller.dto.UpdateSellerDto
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import net.datafaker.Faker
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@AutoConfigureMockMvc
@SpringBootTest()
class SellerControllerTest(
    @Autowired val sellerRepository: SellerRepository,
    @Autowired val accountRepository: AccountRepository,
    @Autowired val mockMvc: MockMvc
) {
    val faker = Faker()

    @Test
    @DisplayName("POST Seller")
    fun postSeller() {
        // given
        val createAccount = Account(
            email = faker.internet().emailAddress(),
            phoneNumber = faker.phoneNumber().phoneNumber(),
            password = faker.internet().password()
        )
        val account = this.accountRepository.save(createAccount)

        val createSellerDto = CreateSellerDto(
            name = faker.name().fullName(),
            address = faker.address().fullAddress()
        )

        val sessionAttr = HashMap<String, Any>()
        sessionAttr[SESSION_NAME] = SessionBody(accountId = account.id!!)

        // when
        val perform = this.mockMvc.perform(
            MockMvcRequestBuilders.post("/sellers")
                .sessionAttrs(sessionAttr)
                .content(jacksonObjectMapper().writeValueAsString(createSellerDto))
                .contentType(MediaType.APPLICATION_JSON)
        )
            // then
            .andExpect(status().isCreated)

        val postSellerResponse = jacksonObjectMapper().readValue(
            perform.andReturn().response.contentAsString,
            PostSellerResponse::class.java
        )

        assertThat(postSellerResponse?.sellerId).isNotNull
    }

    @Test
    @DisplayName("GET seller ME")
    fun getSellerMyself() {
        // given
        val createAccount = Account(
            email = faker.internet().emailAddress(),
            phoneNumber = faker.phoneNumber().phoneNumber(),
            password = faker.internet().password()
        )
        val account = this.accountRepository.save(createAccount)

        val createSeller = Seller(
            name = faker.name().fullName(),
            address = faker.address().fullAddress()
        )
        val seller = this.sellerRepository.save(createSeller)


        val sessionAttr = HashMap<String, Any>()
        sessionAttr[SESSION_NAME] = SessionBody(
            accountId = account.id!!,
            sellerId = seller.id
        )

        val getSellerResponse = GetSellerResponse(
            name = seller.name,
            address = seller.address
        )

        // when
        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/sellers/me")
                .sessionAttrs(sessionAttr)
        )
            // then
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(getSellerResponse)))
    }



    @Test
    @DisplayName("PATCH Seller")
    fun patchSeller() {
        // given
        val createAccount = Account(
            email = faker.internet().emailAddress(),
            phoneNumber = faker.phoneNumber().phoneNumber(),
            password = faker.internet().password()
        )
        val account = this.accountRepository.save(createAccount)

        val createSeller = Seller(
            name = faker.name().fullName(),
            address = faker.address().fullAddress()
        )
        val seller = this.sellerRepository.save(createSeller)

        val sessionAttr = HashMap<String, Any>()
        sessionAttr[SESSION_NAME] = SessionBody(
            accountId = account.id!!,
            sellerId = seller.id
        )

        val updateSellerDto = UpdateSellerDto(
            name = faker.name().username(),
            address = faker.address().fullAddress()
        )

        // when
        this.mockMvc.perform(
            MockMvcRequestBuilders.patch("/sellers/me")
                .sessionAttrs(sessionAttr)
                .content(jacksonObjectMapper().writeValueAsString(updateSellerDto))
                .contentType(MediaType.APPLICATION_JSON)
        )
            // then
            .andExpect(status().isNoContent)

        val updatedSeller = this.sellerRepository.findByIdOrNull(seller.id)

        assertThat(updatedSeller?.name).isEqualTo(updateSellerDto.name)
        assertThat(updatedSeller?.address).isEqualTo(updateSellerDto.address)
    }

    @Test
    @DisplayName("DELETE Seller")
    fun deleteSeller() {
        // given
        val createAccount = Account(
            email = faker.internet().emailAddress(),
            phoneNumber = faker.phoneNumber().phoneNumber(),
            password = faker.internet().password()
        )
        val account = this.accountRepository.save(createAccount)

        val createSeller = Seller(
            name = faker.name().fullName(),
            address = faker.address().fullAddress()
        )
        val seller = this.sellerRepository.save(createSeller)

        val sessionAttr = HashMap<String, Any>()
        sessionAttr[SESSION_NAME] = SessionBody(
            accountId = account.id!!,
            sellerId = seller.id
        )

        // when
        this.mockMvc.perform(
            MockMvcRequestBuilders.delete("/sellers/me")
                .sessionAttrs(sessionAttr)
        )
            // then
            .andExpect(status().isNoContent)

        val deletedSeller = this.sellerRepository.findByIdOrNull(seller.id)

        assertThat(deletedSeller).isNull()
    }
}
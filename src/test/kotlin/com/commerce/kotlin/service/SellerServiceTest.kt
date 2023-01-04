package com.commerce.kotlin.service

import com.commerce.kotlin.dto.CreateCustomerDto
import com.commerce.kotlin.dto.CreateSellerDto
import com.commerce.kotlin.dto.UpdateCustomerDto
import com.commerce.kotlin.dto.UpdateSellerDto
import com.commerce.kotlin.entity.Account
import com.commerce.kotlin.entity.Customer
import com.commerce.kotlin.entity.Seller
import com.commerce.kotlin.repository.AccountRepository
import com.commerce.kotlin.repository.CustomerRepository
import com.commerce.kotlin.repository.SellerRepository
import net.datafaker.Faker
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull


@SpringBootTest
class SellerServiceTest(
    @Autowired private val accountRepository: AccountRepository,
    @Autowired private val sellerRepository: SellerRepository,
    @Autowired private val sellerService: SellerService
) {

    private val faker = Faker()

    fun generateAccount(): Account {
        val account = Account(
            email = faker.internet().emailAddress(),
            phoneNumber = faker.phoneNumber().phoneNumber(),
            password = faker.internet().password()
        )
        return this.accountRepository.save(account)
    }

    fun generateSeller(): Seller {
        val seller = Seller(
            name = faker.name().fullName(),
            address = faker.address().fullAddress()
        )
        return this.sellerRepository.save(seller)
    }

    @Test
    fun createSeller() {
        // given
        val account = this.generateAccount()

        val createSellerDto = CreateSellerDto(
            name = faker.name().fullName(),
            address = faker.address().fullAddress()
        )

        //when
        val sellerId = this.sellerService.createSeller(account.id!!, createSellerDto);
        val customer = this.sellerRepository.findByIdOrNull(sellerId);

        assertThat(customer?.id).isEqualTo(sellerId);
        assertThat(customer?.name).isEqualTo(createSellerDto.name);
        assertThat(customer?.address).isEqualTo(createSellerDto.address);
    }

    @Test
    fun findCustomer() {
        // given
        val seller = generateSeller()
        // when
        val findSeller = this.sellerService.findSeller(seller.id!!)
        // then
        assertThat(seller.id).isEqualTo(findSeller.id)
        assertThat(seller.name).isEqualTo(findSeller.name)
        assertThat(seller.address).isEqualTo(findSeller.address)
    }

    @Test
    fun updateSeller() {
        // given
        val seller = generateSeller()
        val updateSellerDto = UpdateSellerDto(
            name = faker.name().fullName(),
            address = faker.address().fullAddress()
        )

        // when
        this.sellerService.updateSeller(seller.id!!, updateSellerDto);
        val findCustomer = this.sellerRepository.findByIdOrNull(seller.id!!);

        // then
        assertThat(findCustomer?.id).isEqualTo(seller.id)
        assertThat(findCustomer?.name).isEqualTo(updateSellerDto.name)
        assertThat(findCustomer?.address).isEqualTo(updateSellerDto.address)
    }

    @Test
    fun removeSeller() {
        // given
        val seller = generateSeller()
        // when
        this.sellerService.removeSeller(seller.id!!)
        val findSeller = this.sellerRepository.findByIdOrNull(seller.id!!)
        // then
        assertThat(findSeller).isNull()
    }


}
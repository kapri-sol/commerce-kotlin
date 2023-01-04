package com.commerce.kotlin.service

import com.commerce.kotlin.dto.CreateCustomerDto
import com.commerce.kotlin.dto.CreateSellerDto
import com.commerce.kotlin.dto.UpdateCustomerDto
import com.commerce.kotlin.dto.UpdateSellerDto
import com.commerce.kotlin.entity.Customer
import com.commerce.kotlin.entity.Seller
import com.commerce.kotlin.repository.AccountRepository
import com.commerce.kotlin.repository.CustomerRepository
import com.commerce.kotlin.repository.SellerRepository
import jakarta.transaction.Transactional
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Transactional
@Service
class SellerService(
    private val accountRepository: AccountRepository,
    private val sellerRepository: SellerRepository
) {
    fun createSeller(accountId: Long, createSellerDto: CreateSellerDto): Long? {
        val account = accountRepository.findByIdOrNull(accountId) ?: throw ChangeSetPersister.NotFoundException();
        val seller = Seller(
            name = createSellerDto.name,
            address = createSellerDto.address,
        )
        account.setSeller(seller);
        return this.sellerRepository.save(seller).id
    }

    fun findSeller(sellerId: Long): Seller {
        return this.sellerRepository.findByIdOrNull(sellerId) ?: throw ChangeSetPersister.NotFoundException()
    }

    fun updateSeller(sellerId: Long, updateSellerDto: UpdateSellerDto) {
        val seller = this.sellerRepository.findByIdOrNull(sellerId) ?: throw ChangeSetPersister.NotFoundException()
        seller.updateName(updateSellerDto.name)
        seller.updateAddress(updateSellerDto.address)
    }

    fun removeSeller(sellerId: Long) {
        val seller = this.sellerRepository.findByIdOrNull(sellerId) ?: throw ChangeSetPersister.NotFoundException()
        seller.remove()
    }
}
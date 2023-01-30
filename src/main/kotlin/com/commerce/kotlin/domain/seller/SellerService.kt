package com.commerce.kotlin.domain.seller

import com.commerce.kotlin.domain.seller.dto.CreateSellerDto
import com.commerce.kotlin.domain.seller.dto.UpdateSellerDto
import com.commerce.kotlin.domain.account.AccountRepository
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
    fun createSeller(accountId: Long, createSellerDto: CreateSellerDto): Long {
        val account = accountRepository.findByIdOrNull(accountId) ?: throw ChangeSetPersister.NotFoundException()
        val seller = Seller(
            name = createSellerDto.name,
            address = createSellerDto.address,
        )
        account.setSeller(seller)
        return this.sellerRepository.save(seller).id ?: throw IllegalStateException()
    }

    fun findSellerById(sellerId: Long): Seller {
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
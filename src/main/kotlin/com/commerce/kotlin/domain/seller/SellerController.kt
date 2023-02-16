package com.commerce.kotlin.domain.seller

import com.commerce.kotlin.domain.seller.dto.CreateSellerDto
import com.commerce.kotlin.domain.seller.dto.FindSellerResponse
import com.commerce.kotlin.domain.seller.dto.CreateSellerResponse
import com.commerce.kotlin.domain.seller.dto.UpdateSellerDto
import com.commerce.kotlin.security.authentication.CustomUserDetails
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RequestMapping("sellers")
@RestController
class SellerController(
    val sellerService: SellerService
) {
    @GetMapping("me")
    fun getSellerMyself(
        authentication: Authentication
    ): FindSellerResponse {
        val sellerId = (authentication.principal as CustomUserDetails).sellerId ?: throw NotFoundException()
        val seller = this.sellerService.findSellerById(sellerId)
        return FindSellerResponse(name = seller.name, address = seller.address)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postSeller(
        authentication: Authentication,
        @RequestBody createSellerDto: CreateSellerDto
    ): CreateSellerResponse {
        val customUserDetails = authentication.principal as CustomUserDetails
        val accountId = customUserDetails.accountId
        val sellerId = this.sellerService.createSeller(accountId, createSellerDto)

        customUserDetails.setSellerId(sellerId)

        return CreateSellerResponse(
            sellerId = sellerId
        )
    }

    @PatchMapping("me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun patchSeller(
        authentication: Authentication,
        @RequestBody updateSellerDto: UpdateSellerDto
    ) {
        val sellerId = (authentication.principal as CustomUserDetails).sellerId ?: throw NotFoundException()
        this.sellerService.updateSeller(sellerId, updateSellerDto)
    }

    @DeleteMapping("me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteSeller(
        authentication: Authentication,
    ) {
        val sellerId = (authentication.principal as CustomUserDetails).sellerId ?: throw NotFoundException()
        this.sellerService.removeSeller(sellerId)
    }
}
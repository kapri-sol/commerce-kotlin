package com.commerce.kotlin.domain.seller

import com.commerce.kotlin.common.constant.SESSION_NAME
import com.commerce.kotlin.common.constant.SessionBody
import com.commerce.kotlin.domain.seller.dto.CreateSellerDto
import com.commerce.kotlin.domain.seller.dto.PostSellerResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttribute

@RequestMapping("sellers")
@RestController
class SellerController(
    val sellerService: SellerService
) {
    @GetMapping
    fun getSellerMyself() {

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postSeller(
        httpServletRequest: HttpServletRequest,
        @RequestBody createSellerDto: CreateSellerDto
    ): PostSellerResponse {
        val sessionBody = httpServletRequest.session.getAttribute(SESSION_NAME) as SessionBody
        val sellerId = this.sellerService.createSeller(sessionBody.accountId, createSellerDto)

        sessionBody.sellerId = sellerId
        httpServletRequest.session.setAttribute(SESSION_NAME, sessionBody)

        return PostSellerResponse(
            sellerId = sellerId
        )
    }
}
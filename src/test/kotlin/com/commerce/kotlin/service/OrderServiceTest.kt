package com.commerce.kotlin.service

import com.commerce.kotlin.entity.Product
import com.commerce.kotlin.repository.ProductRepository
import net.datafaker.Faker
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class OrderServiceTest(
    @Autowired val productRepository: ProductRepository,
    @Autowired val orderService: OrderService
) {
    val faker = Faker();

    fun generateProduct(): Product {
        val product = Product(
            name = faker.commerce().productName(),
            description = faker.lorem().sentence(),
            price = faker.commerce().price().toDouble(),
            stockQuantity = faker.random().nextInt(1, 100)
        )
        return product
    }

    fun generateProducts(count: Int): List<Product> {
        return List(count){generateProduct()};
    }

    @Test
    fun createOrder() {
        // given
        val products = generateProducts(3);
        // when

        // then
    }

    @Test
    fun findOrder() {
    }
}
package com.commerce.kotlin.order

import com.commerce.kotlin.domain.order.OrderRepository
import com.commerce.kotlin.domain.product.Product
import com.commerce.kotlin.domain.order.OrderService
import com.commerce.kotlin.domain.order.dto.CreateOrderDto
import com.commerce.kotlin.domain.order.dto.CreateOrderItemDto
import com.commerce.kotlin.domain.product.ProductRepository
import net.datafaker.Faker
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class OrderServiceTest(
    @Autowired val productRepository: ProductRepository,
    @Autowired val orderRepository: OrderRepository,
    @Autowired val orderService: OrderService
) {
    val faker = Faker()

    fun generateProduct(): Product {
        val product = Product(
            name = faker.commerce().productName(),
            description = faker.lorem().sentence(),
            price = faker.commerce().price().toDouble(),
            stockQuantity = faker.random().nextInt(11, 100)
        )
        return this.productRepository.save(product)
    }

    fun generateProducts(count: Int): List<Product> {
        return List(count){generateProduct()}
    }

    @Test
    fun createOrder() {
        // given
        val products = generateProducts(faker.random().nextInt(1, 9))
        val createOrderItemsDto = products.map {
            CreateOrderItemDto(
                productId = it.id!!,
                count = 3
            )
        }
        val createOrderDto = CreateOrderDto(orderItems = createOrderItemsDto)

        // when
        val orderId = this.orderService.order(createOrderDto)

        // then
        val findOrder = this.orderRepository.findByIdWithOrderItem(orderId)

        assertThat(findOrder?.id).isEqualTo(orderId)
//        assertThat(findOrder?.orderItems?.size).isEqualTo(products.size)
    }

    @Test
    fun findOrder() {
    }
}
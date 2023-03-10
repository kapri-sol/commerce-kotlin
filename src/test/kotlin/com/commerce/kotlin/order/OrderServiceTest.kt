package com.commerce.kotlin.order

import com.commerce.kotlin.domain.order.OrderRepository
import com.commerce.kotlin.domain.product.Product
import com.commerce.kotlin.domain.order.OrderService
import com.commerce.kotlin.domain.order.dto.CreateOrderDto
import com.commerce.kotlin.domain.order.dto.CreateOrderItemDto
import com.commerce.kotlin.domain.product.ProductRepository
import net.datafaker.Faker
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class OrderServiceTest{
    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Autowired
    private lateinit var orderService: OrderService

    companion object {
        val initialProducts: ArrayList<Product> = arrayListOf()

        val faker = Faker()

        @JvmStatic
        @BeforeAll
        fun init(@Autowired productRepository: ProductRepository): Unit {
            for (i: Int in 1..3) {
                val product = productRepository.save(
                    Product(
                        title = faker.commerce().productName(),
                        description = faker.lorem().sentence(),
                        price = faker.commerce().price().toDouble().toInt(),
                        image = "/",
                        stockQuantity = faker.random().nextInt(1, 100).toInt()
                    )
                )
                initialProducts.add(product)
            }
        }
    }

    @Test
    @DisplayName("주문한다.")
    fun order() {
        // given
        val products = initialProducts
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
}
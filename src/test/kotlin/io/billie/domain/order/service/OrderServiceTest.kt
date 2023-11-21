package io.billie.domain.order.service

import io.billie.application.ports.output.OrderPersistenceOutputPort
import io.billie.domain.order.model.order.Order
import io.billie.domain.order.model.order.OrderState
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@ExtendWith(MockitoExtension::class)
class OrderServiceTest {

    @Mock
    private lateinit var orderPersistenceAdapter: OrderPersistenceOutputPort

    @InjectMocks
    private lateinit var orderService: OrderService

    @Test
    fun `Should Save Order`() {
        //given
        val expectedOrderId = UUID.randomUUID()
        val order = Order(
            UUID.randomUUID(),
            LocalDate.now(),
            BigDecimal.TEN,
            OrderState.PENDING,
            UUID.randomUUID()
        )

        given(orderPersistenceAdapter.saveOrder(order)).willReturn(expectedOrderId)

        //when
        val orderId = orderService.createOrder(order)

        //then
        assertThat(orderId).isNotNull()
        assertThat(orderId).isEqualTo(expectedOrderId)

    }
}
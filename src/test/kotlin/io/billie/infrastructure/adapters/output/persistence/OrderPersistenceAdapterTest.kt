package io.billie.infrastructure.adapters.output.persistence

import io.billie.domain.order.model.order.Order
import io.billie.domain.order.model.order.OrderState
import io.billie.infrastructure.adapters.output.persistence.repository.OrderRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.only
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@ExtendWith(MockitoExtension::class)
class OrderPersistenceAdapterTest {

    @Mock
    private lateinit var orderRepository: OrderRepository

    @InjectMocks
    private lateinit var orderPersistenceAdapter: OrderPersistenceAdapter

    private val expectedOrderId = UUID.randomUUID()

    @Test
    fun `Should Save Order on Repository`() {
        //given
        val order = Order(
            LocalDate.now(),
            BigDecimal.TEN,
            UUID.randomUUID()
        )
        given(orderRepository.saveOrder(order)).willReturn(expectedOrderId)

        //when
        val orderId = orderPersistenceAdapter.saveOrder(order)

        //then
        assertThat(orderId).isNotNull().isEqualTo(expectedOrderId)
        verify(orderRepository, only()).saveOrder(order)
    }

    @Test
    fun `Should Find Order on Repository`() {
        //given
        val orderId = "a19af317-85cb-49db-9a6e-3a3849111a74"
        val order = Order(
            UUID.fromString(orderId),
            LocalDate.now(),
            BigDecimal.TEN,
            OrderState.PENDING,
            UUID.randomUUID()
        )
        given(orderRepository.findOrder(orderId)).willReturn(order)

        //when
        val foundOrder = orderPersistenceAdapter.findOrder(orderId)

        //then
        assertThat(foundOrder).isEqualTo(order)
        verify(orderRepository, only()).findOrder(orderId)
    }

    @Test
    fun `Should Update Order State on Repository`() {
        //given
        val orderId = "a19af317-85cb-49db-9a6e-3a3849111a74"
        given(orderRepository.updateOrderState(orderId, OrderState.COMPLETED)).willReturn(UUID.fromString(orderId))

        //when
        val returnedOrderId = orderPersistenceAdapter.updateOrderState(orderId, OrderState.COMPLETED)

        //then
        assertThat(returnedOrderId.toString()).isEqualTo(orderId)
        verify(orderRepository, only()).updateOrderState(orderId, OrderState.COMPLETED)
    }
}

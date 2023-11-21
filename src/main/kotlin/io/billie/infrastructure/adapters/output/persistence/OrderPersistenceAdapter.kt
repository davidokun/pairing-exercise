package io.billie.infrastructure.adapters.output.persistence

import io.billie.infrastructure.adapters.output.persistence.repository.OrderRepository
import io.billie.domain.order.model.order.Order
import io.billie.domain.order.model.order.OrderState
import io.billie.application.ports.output.OrderPersistenceOutputPort
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrderPersistenceAdapter(
    val orderRepository: OrderRepository
) : OrderPersistenceOutputPort {

    override fun saveOrder(order: Order): UUID {
        return orderRepository.saveOrder(order)
    }

    override fun findOrder(orderId: String): Order? {
        return orderRepository.findOrder(orderId)
    }

    override fun updateOrderState(orderId: String, state: OrderState): UUID {
        return orderRepository.updateOrderState(orderId, state)
    }
}

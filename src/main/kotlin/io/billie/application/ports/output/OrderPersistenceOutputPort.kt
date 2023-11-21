package io.billie.application.ports.output

import io.billie.domain.order.model.order.Order
import io.billie.domain.order.model.order.OrderState
import java.util.*

interface OrderPersistenceOutputPort {

    fun saveOrder(order: Order): UUID

    fun findOrder(orderId: String): Order?

    fun updateOrderState(orderId: String, state: OrderState): UUID
}
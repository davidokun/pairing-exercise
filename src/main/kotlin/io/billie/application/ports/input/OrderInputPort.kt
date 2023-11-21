package io.billie.application.ports.input

import io.billie.domain.order.model.order.Order
import java.util.UUID

interface OrderInputPort {

    fun createOrder(order: Order): UUID
}
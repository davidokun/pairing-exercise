package io.billie.domain.order.service

import io.billie.domain.order.model.order.Order
import io.billie.application.ports.input.OrderInputPort
import io.billie.application.ports.output.OrderPersistenceOutputPort
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrderService(val orderPersistenceAdapter: OrderPersistenceOutputPort) : OrderInputPort {
    override fun createOrder(order: Order): UUID {
        return orderPersistenceAdapter.saveOrder(order)
    }
}

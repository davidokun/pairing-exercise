package io.billie.domain.shipment.service

import io.billie.domain.exception.InvalidShipmentAmount
import io.billie.domain.exception.ShipmentOrderAlreadyCompleted
import io.billie.domain.order.model.order.Order
import io.billie.domain.order.model.order.OrderState
import io.billie.infrastructure.adapters.input.shipment.data.ShipmentNotificationRequest
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class ShipmentNotificationValidator {

    fun validateOrderState(order: Order?) {
        if (order?.state == OrderState.COMPLETED) {
            throw ShipmentOrderAlreadyCompleted("Order ${order.id} was already completed")
        }
    }

    fun validateShipmentAmountWithOrderTotalAmount(
        shipment: ShipmentNotificationRequest,
        order: Order?
    ) {
        if (shipment.amount > order?.totalAmount) {
            throw InvalidShipmentAmount("Shipment amount ${shipment.amount} is greater than the total order amount")
        }
    }

    fun validatePendingOrderAmountWithShipmentAmount(
        shipment: ShipmentNotificationRequest,
        pendingAmount: BigDecimal?
    ) {
        if (shipment.amount > pendingAmount) {
            throw InvalidShipmentAmount("Shipment amount of ${shipment.amount} is greater than the pending order amount of $pendingAmount")
        }
    }

    fun validateIfTotalOrderAmountFulfilled(
        pendingOrderAmount: BigDecimal?,
        shipment: ShipmentNotificationRequest
    ) = pendingOrderAmount?.subtract(shipment.amount)?.compareTo(BigDecimal.ZERO) == 0
}
package io.billie.domain.shipment.service

import io.billie.application.ports.input.ShipmentNotificationInputPort
import io.billie.application.ports.output.OrderPersistenceOutputPort
import io.billie.application.ports.output.ShipmentPersistenceOutputPort
import io.billie.domain.order.model.order.Order
import io.billie.domain.order.model.order.OrderState
import io.billie.domain.shipment.model.Shipment
import io.billie.infrastructure.adapters.input.shipment.data.ShipmentNotificationRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.*

@Service
class ShipmentNotificationService(
    val shipmentPersistence: ShipmentPersistenceOutputPort,
    val orderPersistence: OrderPersistenceOutputPort,
    val validator: ShipmentNotificationValidator
) : ShipmentNotificationInputPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun notify(shipment: ShipmentNotificationRequest): UUID {

        val existingOrder = orderPersistence.findOrder(shipment.orderId)
        validator.validateOrderState(existingOrder)
        validator.validateShipmentAmountWithOrderTotalAmount(shipment, existingOrder)

        val shipments = shipmentPersistence.findShipments(shipment.orderId)
        if (shipments.isEmpty()) {
            return handleInitialShipment(shipment, existingOrder)
        }

        val pendingOrderAmount = calculatePendingOrderAmount(shipments, existingOrder)
        validator.validatePendingOrderAmountWithShipmentAmount(shipment, pendingOrderAmount)

        logger.info("Adding shipment to Order")
        val shipmentId = createShipmentEntry(shipment)

        if (validator.validateIfTotalOrderAmountFulfilled(pendingOrderAmount, shipment)) {
            logger.info("Order amount fulfilled with this shipment. Order was completed")
            completeOrder(shipment)
        }

        return shipmentId

    }

    private fun handleInitialShipment(
        shipment: ShipmentNotificationRequest,
        existingOrder: Order?
    ): UUID {
        logger.info("Not shipment founds yet, creating new one...")
        val shipmentId = createShipmentEntry(shipment)

        if (validator.validateIfTotalOrderAmountFulfilled(existingOrder?.totalAmount, shipment)) {
            completeOrder(shipment)
        }

        return shipmentId
    }

    private fun calculatePendingOrderAmount(
        shipments: List<Shipment>,
        existingOrder: Order?
    ): BigDecimal? {
        val currentShippedAmount = shipments
            .map { s -> s.amount }
            .reduce { a, n -> a.add(n) }

        return existingOrder?.totalAmount?.subtract(currentShippedAmount)
    }

    private fun createShipmentEntry(shipment: ShipmentNotificationRequest): UUID {
        return shipmentPersistence.createShipment(shipment)
    }

    private fun completeOrder(shipment: ShipmentNotificationRequest): UUID {
        return orderPersistence.updateOrderState(shipment.orderId, OrderState.COMPLETED)
    }

}

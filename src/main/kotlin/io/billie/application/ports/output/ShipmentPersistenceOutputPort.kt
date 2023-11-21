package io.billie.application.ports.output

import io.billie.domain.shipment.model.Shipment
import io.billie.infrastructure.adapters.input.shipment.data.ShipmentNotificationRequest
import java.util.*

interface ShipmentPersistenceOutputPort {

    fun createShipment(shipment: ShipmentNotificationRequest): UUID

    fun findShipments(orderId: String): List<Shipment>
}
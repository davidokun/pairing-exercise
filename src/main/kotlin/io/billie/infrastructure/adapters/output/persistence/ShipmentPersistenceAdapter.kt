package io.billie.infrastructure.adapters.output.persistence

import io.billie.application.ports.output.ShipmentPersistenceOutputPort
import io.billie.infrastructure.adapters.output.persistence.repository.ShipmentRepository
import io.billie.domain.shipment.model.Shipment
import io.billie.infrastructure.adapters.input.shipment.data.ShipmentNotificationRequest
import org.springframework.stereotype.Service
import java.util.*

@Service
class ShipmentPersistenceAdapter(
    val shipmentRepository: ShipmentRepository
) : ShipmentPersistenceOutputPort {

    override fun createShipment(shipment: ShipmentNotificationRequest): UUID {
        return shipmentRepository.createShipment(shipment)
    }

    override fun findShipments(orderId: String): List<Shipment> {
        return shipmentRepository.findOrderShipments(orderId)
    }
}
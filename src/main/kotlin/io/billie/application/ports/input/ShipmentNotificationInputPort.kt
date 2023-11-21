package io.billie.application.ports.input

import io.billie.infrastructure.adapters.input.shipment.data.ShipmentNotificationRequest
import java.util.*

interface ShipmentNotificationInputPort {

    fun notify(shipment: ShipmentNotificationRequest): UUID
}
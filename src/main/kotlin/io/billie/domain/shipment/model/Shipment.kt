package io.billie.domain.shipment.model

import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

class Shipment(
    val id: UUID,
    val orderId: UUID,
    val shipmentDate: LocalDate,
    val amount: BigDecimal
)
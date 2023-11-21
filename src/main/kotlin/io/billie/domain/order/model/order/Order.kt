package io.billie.domain.order.model.order

import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

class Order(
    val id: UUID?,
    val orderDate: LocalDate,
    val totalAmount: BigDecimal,
    val state: OrderState,
    val organisationId: UUID
) {
    constructor(
        orderDate: LocalDate,
        totalAmount: BigDecimal,
        organisationId: UUID) : this(null, orderDate, totalAmount, OrderState.PENDING, organisationId)
}
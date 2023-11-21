package io.billie.domain.shipment.service

import io.billie.domain.exception.InvalidShipmentAmount
import io.billie.domain.exception.ShipmentOrderAlreadyCompleted
import io.billie.domain.order.model.order.Order
import io.billie.domain.order.model.order.OrderState
import io.billie.infrastructure.adapters.input.shipment.data.ShipmentNotificationRequest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

class ShipmentNotificationValidatorTest {

    private val validator = ShipmentNotificationValidator()

    @Test
    fun `Should Validate If Order State Is Completed And Throw`() {
        //given
        val order = Order(
            UUID.randomUUID(),
            LocalDate.now(),
            BigDecimal.TEN,
            OrderState.COMPLETED,
            UUID.randomUUID()
        )

        //then
        assertThatCode { validator.validateOrderState(order) }
            .isExactlyInstanceOf(ShipmentOrderAlreadyCompleted::class.java)
    }

    @Test
    fun `Should Validate If Order State Is Pending And Do Nothing`() {
        //given
        val order = Order(
            UUID.randomUUID(),
            LocalDate.now(),
            BigDecimal.TEN,
            OrderState.PENDING,
            UUID.randomUUID()
        )

        //then
        assertThatCode { validator.validateOrderState(order) }
            .doesNotThrowAnyException()
    }

    @Test
    fun `Should Validate Shipment Amount and Total Order Amount And Throw If Shipment is Greater`() {
        //give
        val shipment = ShipmentNotificationRequest(
            "635bbb2f-abb3-434d-b24a-fd238cbe02c9",
            BigDecimal("100.01"),
            LocalDate.now()
        )

        val order = Order(
            UUID.randomUUID(),
            LocalDate.now(),
            BigDecimal("100.00"),
            OrderState.PENDING,
            UUID.randomUUID()
        )

        //then
        assertThatCode { validator.validateShipmentAmountWithOrderTotalAmount(shipment, order) }
            .isExactlyInstanceOf(InvalidShipmentAmount::class.java)
    }

    @Test
    fun `Should Validate Shipment Amount and Total Order Amount And Don't Throw If Valid`() {
        //give
        val shipment = ShipmentNotificationRequest(
            "635bbb2f-abb3-434d-b24a-fd238cbe02c9",
            BigDecimal("100.00"),
            LocalDate.now()
        )

        val order = Order(
            UUID.randomUUID(),
            LocalDate.now(),
            BigDecimal("100.00"),
            OrderState.PENDING,
            UUID.randomUUID()
        )

        //then
        assertThatCode { validator.validateShipmentAmountWithOrderTotalAmount(shipment, order) }
            .doesNotThrowAnyException()
    }

    @Test
    fun `Should Validate Shipment Amount and Pending Order Amount And Throw If Grater`() {
        //give
        val shipment = ShipmentNotificationRequest(
            "635bbb2f-abb3-434d-b24a-fd238cbe02c9",
            BigDecimal("10.01"),
            LocalDate.now()
        )

        val pendingOrderAmount = BigDecimal(10.00)

        //then
        assertThatCode { validator.validatePendingOrderAmountWithShipmentAmount(shipment, pendingOrderAmount) }
            .isExactlyInstanceOf(InvalidShipmentAmount::class.java)
    }

    @Test
    fun `Should Validate Shipment Amount and Pending Order Amount And Don't Throw If Valid`() {
        //give
        val shipment = ShipmentNotificationRequest(
            "635bbb2f-abb3-434d-b24a-fd238cbe02c9",
            BigDecimal("10.00"),
            LocalDate.now()
        )

        val pendingOrderAmount = BigDecimal(10.00)

        //then
        assertThatCode { validator.validatePendingOrderAmountWithShipmentAmount(shipment, pendingOrderAmount) }
            .doesNotThrowAnyException()
    }

    @Test
    fun `Should Validate If Total Order Amount Was Fulfilled`() {
        //give
        val shipment = ShipmentNotificationRequest(
            "635bbb2f-abb3-434d-b24a-fd238cbe02c9",
            BigDecimal("10.00"),
            LocalDate.now()
        )

        val pendingOrderAmount = BigDecimal(10.00)

        //when
        val isFulfilled =
            validator.validateIfTotalOrderAmountFulfilled(pendingOrderAmount, shipment)

        //then
        assertThat(isFulfilled).isTrue()
    }

    @Test
    fun `Should Validate If Total Order Amount Was Not Fulfilled Yet`() {
        //give
        val shipment = ShipmentNotificationRequest(
            "635bbb2f-abb3-434d-b24a-fd238cbe02c9",
            BigDecimal("9.99"),
            LocalDate.now()
        )

        val pendingOrderAmount = BigDecimal(10.00)

        //when
        val isFulfilled =
            validator.validateIfTotalOrderAmountFulfilled(pendingOrderAmount, shipment)

        //then
        assertThat(isFulfilled).isFalse()
    }
}
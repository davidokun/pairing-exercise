package io.billie.domain.shipment.service

import io.billie.application.ports.output.OrderPersistenceOutputPort
import io.billie.application.ports.output.ShipmentPersistenceOutputPort
import io.billie.domain.order.model.order.Order
import io.billie.domain.order.model.order.OrderState
import io.billie.domain.shipment.model.Shipment
import io.billie.infrastructure.adapters.input.shipment.data.ShipmentNotificationRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@ExtendWith(MockitoExtension::class)
class ShipmentNotificationServiceTest {

    @Mock
    private lateinit var shipmentPersistence: ShipmentPersistenceOutputPort

    @Mock
    private lateinit var orderPersistence: OrderPersistenceOutputPort

    @Mock
    private lateinit var validator: ShipmentNotificationValidator

    @InjectMocks
    private lateinit var service: ShipmentNotificationService

    @Test
    fun `Should Create Initial Shipment Correctly And Not Complete The Order`() {
        //given
        val orderId = "635bbb2f-abb3-434d-b24a-fd238cbe02c9"
        val expectedShipmentId = UUID.fromString("a19af317-85cb-49db-9a6e-3a3849111a74")
        val shipment = ShipmentNotificationRequest(
            orderId,
            BigDecimal.TEN,
            LocalDate.now()
        )
        val order = buildOrder(orderId, BigDecimal("100.00"), OrderState.PENDING)

        given(orderPersistence.findOrder(orderId)).willReturn(order)
        given(shipmentPersistence.createShipment(shipment)).willReturn(expectedShipmentId)

        //when
        val shipmentId = service.notify(shipment)

        //then
        assertThat(shipmentId).isNotNull
        assertThat(shipmentId).isEqualTo(expectedShipmentId)
        verify(orderPersistence, only()).findOrder(orderId)
        verify(shipmentPersistence).createShipment(shipment)
        verify(orderPersistence, never()).updateOrderState(orderId, OrderState.COMPLETED)
    }

    @Test
    fun `Should Create Initial Shipment Correctly And Complete The Order If Fulfilled`() {
        //given
        val orderId = "635bbb2f-abb3-434d-b24a-fd238cbe02c9"
        val expectedShipmentId = UUID.fromString("a19af317-85cb-49db-9a6e-3a3849111a74")
        val shipment = ShipmentNotificationRequest(
            orderId,
            BigDecimal("100.00"),
            LocalDate.now()
        )
        val order = buildOrder(orderId, BigDecimal("100.00"), OrderState.PENDING)

        given(orderPersistence.findOrder(orderId)).willReturn(order)
        given(shipmentPersistence.createShipment(shipment)).willReturn(expectedShipmentId)
        given(validator.validateIfTotalOrderAmountFulfilled(BigDecimal("100.00"), shipment)).willReturn(true)

        //when
        val shipmentId = service.notify(shipment)

        //then
        assertThat(shipmentId).isNotNull
        assertThat(shipmentId).isEqualTo(expectedShipmentId)
        verify(orderPersistence).findOrder(orderId)
        verify(shipmentPersistence).createShipment(shipment)
        verify(orderPersistence).updateOrderState(orderId, OrderState.COMPLETED)

    }

    @Test
    fun `Should Create Additional Shipment Correctly And Not Complete The Order`() {
        //given
        val orderId = "635bbb2f-abb3-434d-b24a-fd238cbe02c9"
        val expectedShipmentId = UUID.fromString("a19af317-85cb-49db-9a6e-3a3849111a74")
        val shipment = ShipmentNotificationRequest(
            orderId,
            BigDecimal("30.00"),
            LocalDate.now()
        )
        val order = buildOrder(orderId, BigDecimal("100.00"), OrderState.PENDING)

        given(orderPersistence.findOrder(orderId)).willReturn(order)
        given(shipmentPersistence.findShipments(orderId)).willReturn(listOf(buildShipment(orderId, "30.00")))
        given(shipmentPersistence.createShipment(shipment)).willReturn(expectedShipmentId)

        //when
        val shipmentId = service.notify(shipment)

        //then
        assertThat(shipmentId).isNotNull
        assertThat(shipmentId).isEqualTo(expectedShipmentId)
        verify(orderPersistence, only()).findOrder(orderId)
        verify(shipmentPersistence).findShipments(orderId)
        verify(shipmentPersistence).createShipment(shipment)
        verify(orderPersistence, never()).updateOrderState(orderId, OrderState.COMPLETED)

    }

    @Test
    fun `Should Create Additional Shipment Correctly And Complete The Order As Fulfilled`() {
        //given
        val orderId = "635bbb2f-abb3-434d-b24a-fd238cbe02c9"
        val expectedShipmentId = UUID.fromString("a19af317-85cb-49db-9a6e-3a3849111a74")
        val shipment = ShipmentNotificationRequest(
            orderId,
            BigDecimal("50.00"),
            LocalDate.now()
        )
        val order = buildOrder(orderId, BigDecimal("100.00"), OrderState.PENDING)

        given(orderPersistence.findOrder(orderId)).willReturn(order)
        given(shipmentPersistence.findShipments(orderId)).willReturn(listOf(buildShipment(orderId, "50.00")))
        given(shipmentPersistence.createShipment(shipment)).willReturn(expectedShipmentId)
        given(validator.validateIfTotalOrderAmountFulfilled(BigDecimal("50.00"), shipment)).willReturn(true)

        //when
        val shipmentId = service.notify(shipment)

        //then
        assertThat(shipmentId).isNotNull
        assertThat(shipmentId).isEqualTo(expectedShipmentId)
        verify(orderPersistence).findOrder(orderId)
        verify(shipmentPersistence).findShipments(orderId)
        verify(shipmentPersistence).createShipment(shipment)
        verify(orderPersistence).updateOrderState(orderId, OrderState.COMPLETED)

    }

    private fun buildOrder(
        orderId: String,
        totalAmount: BigDecimal,
        state: OrderState
    ): Order {
        return Order(
            UUID.fromString(orderId),
            LocalDate.now(),
            totalAmount,
            state,
            UUID.randomUUID()
        )
    }

    private fun buildShipment(orderId: String, amount: String): Shipment {
        return Shipment(
            UUID.randomUUID(),
            UUID.fromString(orderId),
            LocalDate.now(),
            BigDecimal(amount)
        )
    }
}
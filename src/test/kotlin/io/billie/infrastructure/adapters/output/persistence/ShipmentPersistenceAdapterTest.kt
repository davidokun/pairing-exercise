package io.billie.infrastructure.adapters.output.persistence

import io.billie.domain.shipment.model.Shipment
import io.billie.infrastructure.adapters.input.shipment.data.ShipmentNotificationRequest
import io.billie.infrastructure.adapters.output.persistence.repository.ShipmentRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.only
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@ExtendWith(MockitoExtension::class)
class ShipmentPersistenceAdapterTest {

    @Mock
    private lateinit var shipmentRepository: ShipmentRepository

    @InjectMocks
    private lateinit var shipmentPersistenceAdapter: ShipmentPersistenceAdapter

    private val expectedShipmentId = UUID.randomUUID()

    @Test
    fun `Should Create Shipment on Repository`() {
        //given
        val orderId = "a19af317-85cb-49db-9a6e-3a3849111a74"
        val shipmentRequest = ShipmentNotificationRequest(
            orderId,
            BigDecimal.TEN,
            LocalDate.now()
        )
        given(shipmentRepository.createShipment(shipmentRequest)).willReturn(expectedShipmentId)

        //when
        val createShipmentId = shipmentPersistenceAdapter.createShipment(shipmentRequest)

        //then
        assertThat(createShipmentId).isEqualTo(expectedShipmentId)
        verify(shipmentRepository, only()).createShipment(shipmentRequest)
    }

    @Test
    fun `Should Find Shipments on Repository`() {
        //given
        val orderId = "a19af317-85cb-49db-9a6e-3a3849111a74"
        val shipment = Shipment(
            expectedShipmentId,
            UUID.fromString(orderId),
            LocalDate.now(),
            BigDecimal.TEN
        )
        given(shipmentRepository.findOrderShipments(orderId)).willReturn(listOf(shipment))

        //when
        val foundShipments = shipmentPersistenceAdapter.findShipments(orderId)

        //then
        assertThat(foundShipments).hasSize(1)
        verify(shipmentRepository, only()).findOrderShipments(orderId)
    }
}

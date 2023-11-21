package io.billie.functional

import com.fasterxml.jackson.databind.ObjectMapper
import io.billie.domain.order.model.order.OrderState
import io.billie.domain.shipment.model.Shipment
import io.billie.functional.data.Fixtures
import io.billie.infrastructure.adapters.input.common.Entity
import io.billie.infrastructure.adapters.input.exception.ApiError
import io.billie.infrastructure.adapters.output.persistence.repository.OrderRepository
import io.billie.infrastructure.adapters.output.persistence.repository.ShipmentRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDate

@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@SqlGroup(
    Sql(scripts = ["classpath:/shipments_test_data.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
    Sql(scripts = ["classpath:/clean_up.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
)
class CanCreateShipments {

    @LocalServerPort
    private val port = 8080

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var shipmentRepository: ShipmentRepository

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Test
    fun `Should Create Shipment For Total Order Amount And Complete the Order`() {
        //given
        val orderId = "635bbb2f-abb3-434d-b24a-fd238cbe02c9"

        //when
        val shipmentAmount = "100.00"
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/shipments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Fixtures.shipmentRequestJsonOk(orderId, shipmentAmount))
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andReturn()

        //then
        val response = mapper.readValue(result.response.contentAsString, Entity::class.java)
        val shipments = shipmentRepository.findOrderShipments(orderId)
        val order = orderRepository.findOrder(orderId)

        val shipment = shipments[0]

        assertAll(
            { assertThat(response.id).isNotNull },
            { assertThat(shipment).isNotNull },
            { assertThat(shipment.id).isNotNull },
            { assertThat(shipment.orderId.toString()).isEqualTo(orderId) },
            { assertThat(shipment.amount.toString()).isEqualTo(shipmentAmount) },
            { assertThat(shipment.shipmentDate).isEqualTo(LocalDate.parse("2023-11-20")) },
            { assertThat(order?.state).isEqualTo(OrderState.COMPLETED) },
        )
    }

    @Test
    fun `Should Create Shipment For Partial Order Amount And The Order Is Still Pending`() {
        //given
        val orderId = "635bbb2f-abb3-434d-b24a-fd238cbe02c9"

        //when
        val shipmentAmount = "30.00"
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/shipments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Fixtures.shipmentRequestJsonOk(orderId, shipmentAmount))
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andReturn()

        //then
        val response = mapper.readValue(result.response.contentAsString, Entity::class.java)
        val shipments = shipmentRepository.findOrderShipments(orderId)
        val order = orderRepository.findOrder(orderId)

        val shipment = shipments[0]

        assertAll(
            { assertThat(response.id).isNotNull },
            { assertThat(shipment).isNotNull },
            { assertThat(shipment.id).isNotNull },
            { assertThat(shipment.orderId.toString()).isEqualTo(orderId) },
            { assertThat(shipment.amount.toString()).isEqualTo(shipmentAmount) },
            { assertThat(shipment.shipmentDate).isEqualTo(LocalDate.parse("2023-11-20")) },
            { assertThat(order?.state).isEqualTo(OrderState.PENDING) },
        )
    }

    @Test
    fun `Should Create Shipment For Last Pending Amount And Then Complete The Order`() {
        //given
        val orderId = "ffe27d9b-bfe9-4db1-884a-75630da6aeb1"

        //when
        val shipmentAmount = "10.00"
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/shipments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Fixtures.shipmentRequestJsonOk(orderId, shipmentAmount))
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andReturn()

        //then
        val response = mapper.readValue(result.response.contentAsString, Entity::class.java)
        val shipments = shipmentRepository.findOrderShipments(orderId)
        val order = orderRepository.findOrder(orderId)

        assertThat(response.id).isNotNull
        assertThat(shipments).hasSize(4)
        assertThat(shipments).extracting(Shipment::amount).containsExactlyInAnyOrder(
            Tuple(BigDecimal("30.00")),
            Tuple(BigDecimal("30.00")),
            Tuple(BigDecimal("30.00")),
            Tuple(BigDecimal("10.00")),
        )
        assertThat(order?.state).isEqualTo(OrderState.COMPLETED)
    }

    @Test
    fun `Should Not Create Shipment For Last Pending Amount If Shipment Amount Is Greater Than Pending Amount`() {
        //given
        val orderId = "ffe27d9b-bfe9-4db1-884a-75630da6aeb1"

        //when
        val shipmentAmount = "10.01"
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/shipments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Fixtures.shipmentRequestJsonOk(orderId, shipmentAmount))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andReturn()

        //then
        val response = mapper.readValue(result.response.contentAsString, ApiError::class.java)
        assertAll(
            { assertThat(response.message).isEqualTo("Shipment amount of 10.01 is greater than the pending order amount of 10.00") },
            { assertThat(response.status).isEqualTo(400) }
        )
    }

    @Test
    fun `Should Not Create Shipment If Order Was Already Completed`() {
        //given
        val orderId = "482152c5-e92d-4fa7-bee3-a8ac2031a701"
        val shipmentAmount = "50.00"

        //when
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/shipments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Fixtures.shipmentRequestJsonOk(orderId, shipmentAmount))
        )
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andReturn()

        //then
        val response = mapper.readValue(result.response.contentAsString, ApiError::class.java)
        assertAll(
            { assertThat(response.message).isEqualTo("Order 482152c5-e92d-4fa7-bee3-a8ac2031a701 was already completed") },
            { assertThat(response.status).isEqualTo(409) }
        )
    }

    @Test
    fun `Should Not Create Shipment If Shipment Amount Is Greater Than Total Order Amount`() {
        //given
        val orderId = "ffe27d9b-bfe9-4db1-884a-75630da6aeb1"
        val shipmentAmount = "150.00"

        //when
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/shipments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Fixtures.shipmentRequestJsonOk(orderId, shipmentAmount))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andReturn()

        //then
        val response = mapper.readValue(result.response.contentAsString, ApiError::class.java)

        assertAll(
            { assertThat(response.message).isEqualTo("Shipment amount 150.00 is greater than the total order amount") },
            { assertThat(response.status).isEqualTo(400) }
        )
    }


}
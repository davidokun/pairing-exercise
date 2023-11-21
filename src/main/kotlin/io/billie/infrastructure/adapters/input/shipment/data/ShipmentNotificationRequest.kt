package io.billie.infrastructure.adapters.input.shipment.data

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Table("SHIPMENTS")
class ShipmentNotificationRequest(
    @field:NotBlank @JsonProperty("order_id") val orderId: String,
    @field:NotNull @JsonProperty("amount") val amount: BigDecimal,
    @JsonFormat(pattern = "dd/MM/yyyy") @JsonProperty("shipment_date") val shipmentDate: LocalDate
)

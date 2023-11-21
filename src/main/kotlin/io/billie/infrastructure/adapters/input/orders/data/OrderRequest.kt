package io.billie.infrastructure.adapters.input.orders.data

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Table("ORDERS")
data class OrderRequest(
    @field:NotBlank @JsonProperty("organisation_id") val organisationId: String,
    @field:NotNull @JsonProperty("order_amount") val orderAmount: BigDecimal,
    @JsonFormat(pattern = "dd/MM/yyyy") @JsonProperty("order_date") val orderDate: LocalDate
)

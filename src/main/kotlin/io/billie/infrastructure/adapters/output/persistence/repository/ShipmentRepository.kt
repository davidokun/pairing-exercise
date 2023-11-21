package io.billie.infrastructure.adapters.output.persistence.repository

import io.billie.domain.shipment.model.Shipment
import io.billie.infrastructure.adapters.input.shipment.data.ShipmentNotificationRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.sql.Date
import java.sql.ResultSet
import java.util.*

@Repository
class ShipmentRepository {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Transactional(readOnly = true)
    fun findOrderShipments(orderId: String): List<Shipment> {
        return jdbcTemplate.query(
            "SELECT id, order_id, shipment_date, amount from organisations_schema.shipments WHERE order_id = ?",
            shipmentMapper(),
            UUID.fromString(orderId)
        )
    }

    fun createShipment(shipment: ShipmentNotificationRequest): UUID {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(
            { connection ->
                val ps = connection.prepareStatement(
                    "INSERT INTO organisations_schema.shipments (" +
                            "order_id, " +
                            "shipment_date, " +
                            "amount" +
                            ") VALUES (?, ?, ?)",
                    arrayOf("id")
                )
                ps.setObject(1, UUID.fromString(shipment.orderId))
                ps.setDate(2, Date.valueOf(shipment.shipmentDate))
                ps.setBigDecimal(3, shipment.amount)
                ps
            }, keyHolder
        )
        return keyHolder.getKeyAs(UUID::class.java)!!
    }

    private fun shipmentMapper() = RowMapper<Shipment> { it: ResultSet, _: Int ->
        Shipment(
            it.getObject("id", UUID::class.java),
            it.getObject("order_id", UUID::class.java),
            Date(it.getDate("shipment_date").time).toLocalDate(),
            it.getObject("amount", BigDecimal::class.java)
        )
    }
}
package io.billie.infrastructure.adapters.output.persistence.repository

import io.billie.domain.order.model.order.Order
import io.billie.domain.order.model.order.OrderState
import io.billie.domain.exception.InvalidOrganisationId
import io.billie.domain.exception.OrderNotFound
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
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
class OrderRepository {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Transactional
    fun saveOrder(order: Order): UUID {
        try {
            return createOrder(order)
        } catch (ex: DataIntegrityViolationException) {
            throw InvalidOrganisationId(order.organisationId.toString())
        }
    }

    @Transactional(readOnly = true)
    fun findOrder(orderId: String): Order? {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT id, order_date, total_amount, state, organisation_id FROM organisations_schema.orders WHERE id = ?",
                orderMapper(),
                UUID.fromString(orderId)
            )
        } catch (ex: EmptyResultDataAccessException) {
            throw OrderNotFound(orderId)
        }
    }

    @Transactional
    fun updateOrderState(orderId: String, state: OrderState): UUID {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(
            { connection ->
                val ps = connection.prepareStatement(
                    "UPDATE organisations_schema.orders SET state = ? WHERE id = ?",
                    arrayOf("id")
                )
                ps.setString(1, state.name)
                ps.setObject(2, UUID.fromString(orderId))
                ps
            }, keyHolder
        )
        return keyHolder.getKeyAs(UUID::class.java)!!
    }

    private fun createOrder(order: Order): UUID {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(
            { connection ->
                val ps = connection.prepareStatement(
                    "INSERT INTO organisations_schema.orders (" +
                            "order_date, " +
                            "total_amount, " +
                            "state, " +
                            "organisation_id" +
                            ") VALUES (?, ?, ?, ?)",
                    arrayOf("id")
                )
                ps.setDate(1, Date.valueOf(order.orderDate))
                ps.setBigDecimal(2, order.totalAmount)
                ps.setString(3, OrderState.PENDING.name)
                ps.setObject(4, order.organisationId)
                ps
            }, keyHolder
        )
        return keyHolder.getKeyAs(UUID::class.java)!!
    }

    private fun orderMapper() = RowMapper<Order> { it: ResultSet, _: Int ->
        Order(
            it.getObject("id", UUID::class.java),
            Date(it.getDate("order_date").time).toLocalDate(),
            it.getObject("total_amount", BigDecimal::class.java),
            OrderState.valueOf(it.getString("state")),
            it.getObject("organisation_id", UUID::class.java)
        )
    }


}
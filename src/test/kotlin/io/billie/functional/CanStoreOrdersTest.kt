package io.billie.functional

import com.fasterxml.jackson.databind.ObjectMapper
import io.billie.functional.data.Fixtures
import io.billie.infrastructure.adapters.input.common.Entity
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsEqual
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*

@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CanStoreOrdersTest {

    @LocalServerPort
    private val port = 8080

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var template: JdbcTemplate

    private lateinit var orgId: UUID

    @BeforeAll
    fun setUp() {
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/organisations")
                .contentType(MediaType.APPLICATION_JSON).content(Fixtures.orgRequestJson())
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        orgId = mapper.readValue(result.response.contentAsString, Entity::class.java).id
    }

    @Test
    fun `Should Create New Order`() {
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/orders")
                .contentType(MediaType.APPLICATION_JSON).content(Fixtures.orderRequestJsonOk(orgId.toString()))
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andReturn()

        val response = mapper.readValue(result.response.contentAsString, Entity::class.java)
        val order: Map<String, Any> = orgFromDatabase(response.id)

        assertDataMatches(order, Fixtures.bbcOrderFixture(response.id, orgId))
    }

    @Test
    fun `Should Not Create New Order For A Non Existing Organisation`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Fixtures.orderRequestJsonOk(UUID.randomUUID().toString()))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    fun assertDataMatches(reply: Map<String, Any>, assertions: Map<String, Any>) {
        for (key in assertions.keys) {
            MatcherAssert.assertThat(reply[key], IsEqual.equalTo(assertions[key]))
        }
    }

    private fun queryEntityFromDatabase(sql: String, id: UUID): MutableMap<String, Any> =
        template.queryForMap(sql, id)

    private fun orgFromDatabase(id: UUID): MutableMap<String, Any> =
        queryEntityFromDatabase("select * from organisations_schema.orders where id = ?", id)

}
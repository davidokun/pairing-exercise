package io.billie.application.ports.output

import io.billie.infrastructure.adapters.input.organisations.data.OrganisationRequest
import io.billie.infrastructure.adapters.input.organisations.data.OrganisationResponse
import java.util.*

interface OrganisationPersistenceOutputPort {

    fun create(organisation: OrganisationRequest): UUID

    fun findOrganisations(): List<OrganisationResponse>
}
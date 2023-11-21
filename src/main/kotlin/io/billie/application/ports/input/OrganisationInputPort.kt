package io.billie.application.ports.input

import io.billie.infrastructure.adapters.input.organisations.data.OrganisationRequest
import io.billie.infrastructure.adapters.input.organisations.data.OrganisationResponse
import java.util.*

interface OrganisationInputPort {

    fun createOrganisation(organisation: OrganisationRequest): UUID

    fun findOrganisations(): List<OrganisationResponse>
}
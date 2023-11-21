package io.billie.domain.organisation.service

import io.billie.infrastructure.adapters.input.organisations.data.OrganisationRequest
import io.billie.infrastructure.adapters.input.organisations.data.OrganisationResponse
import io.billie.application.ports.input.OrganisationInputPort
import io.billie.application.ports.output.OrganisationPersistenceOutputPort
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrganisationService(
    val organisationPersistenceAdapter: OrganisationPersistenceOutputPort
) : OrganisationInputPort {

    override fun findOrganisations(): List<OrganisationResponse> = organisationPersistenceAdapter.findOrganisations()

    override fun createOrganisation(organisation: OrganisationRequest): UUID {
        return organisationPersistenceAdapter.create(organisation)
    }

}

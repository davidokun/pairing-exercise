package io.billie.infrastructure.adapters.output.persistence

import io.billie.infrastructure.adapters.input.organisations.data.OrganisationRequest
import io.billie.infrastructure.adapters.input.organisations.data.OrganisationResponse
import io.billie.infrastructure.adapters.output.persistence.repository.OrganisationRepository
import io.billie.application.ports.output.OrganisationPersistenceOutputPort
import org.springframework.stereotype.Component
import java.util.*

@Component
class OrganisationPersistenceAdapter(
    val organisationRepository: OrganisationRepository
): OrganisationPersistenceOutputPort {

    override fun create(organisation: OrganisationRequest): UUID {
        return organisationRepository.create(organisation)
    }

    override fun findOrganisations(): List<OrganisationResponse> {
        return organisationRepository.findOrganisations()
    }
}
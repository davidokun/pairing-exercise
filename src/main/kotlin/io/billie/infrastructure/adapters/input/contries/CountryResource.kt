package io.billie.infrastructure.adapters.input.contries

import io.billie.application.ports.input.CountryInputPort
import io.billie.infrastructure.adapters.input.contries.data.CityResponse
import io.billie.infrastructure.adapters.input.contries.data.CountryResponse
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("countries")
class CountryResource(val service: CountryInputPort) {

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "All countries",
                content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = CountryResponse::class)))
                    ))]
            )]
    )
    @GetMapping
    fun index(): List<CountryResponse> = service.findCountries()

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Found cities for country",
                content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = CityResponse::class)))
                    ))]
            ),
            ApiResponse(responseCode = "404", description = "No cities found for country code", content = [Content()])]
    )
    @GetMapping("/{countryCode}/cities")
    fun cities(@PathVariable("countryCode") countryCode: String): List<CityResponse> {
        val cities = service.findCities(countryCode.uppercase())
        if (cities.isEmpty()) {
            throw ResponseStatusException(
                NOT_FOUND,
                "No cities found for $countryCode"
            )
        }
        return cities
    }

}

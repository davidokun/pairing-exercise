package io.billie.functional.data

import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

object Fixtures {

    fun orgRequestJsonNameBlank(): String {
        return "{\n" +
                "  \"name\": \"\",\n" +
                "  \"date_founded\": \"18/10/1922\",\n" +
                "  \"country_code\": \"GB\",\n" +
                "  \"vat_number\": \"333289454\",\n" +
                "  \"registration_number\": \"3686147\",\n" +
                "  \"legal_entity_type\": \"NONPROFIT_ORGANIZATION\",\n" +
                "  \"contact_details\": {\n" +
                "    \"phone_number\": \"+443700100222\",\n" +
                "    \"fax\": \"\",\n" +
                "    \"email\": \"yourquestions@bbc.co.uk\"\n" +
                "  }\n" +
                "}"
    }

    fun orgRequestJsonNoName(): String {
        return "{\n" +
                "  \"date_founded\": \"18/10/1922\",\n" +
                "  \"country_code\": \"GB\",\n" +
                "  \"vat_number\": \"333289454\",\n" +
                "  \"registration_number\": \"3686147\",\n" +
                "  \"legal_entity_type\": \"NONPROFIT_ORGANIZATION\",\n" +
                "  \"contact_details\": {\n" +
                "    \"phone_number\": \"+443700100222\",\n" +
                "    \"fax\": \"\",\n" +
                "    \"email\": \"yourquestions@bbc.co.uk\"\n" +
                "  }\n" +
                "}"
    }

    fun orgRequestJsonNoLegalEntityType(): String {
        return "{\n" +
                "  \"name\": \"BBC\",\n" +
                "  \"date_founded\": \"18/10/1922\",\n" +
                "  \"country_code\": \"GB\",\n" +
                "  \"vat_number\": \"333289454\",\n" +
                "  \"registration_number\": \"3686147\",\n" +
                "  \"contact_details\": {\n" +
                "    \"phone_number\": \"+443700100222\",\n" +
                "    \"fax\": \"\",\n" +
                "    \"email\": \"yourquestions@bbc.co.uk\"\n" +
                "  }\n" +
                "}"
    }

    fun orgRequestJsonNoContactDetails(): String {
        return "{\n" +
                "  \"name\": \"BBC\",\n" +
                "  \"date_founded\": \"18/10/1922\",\n" +
                "  \"country_code\": \"GB\",\n" +
                "  \"vat_number\": \"333289454\",\n" +
                "  \"registration_number\": \"3686147\",\n" +
                "  \"legal_entity_type\": \"NONPROFIT_ORGANIZATION\"\n" +
                "}"
    }

    fun orgRequestJson(): String {
        return "{\n" +
                "  \"name\": \"BBC\",\n" +
                "  \"date_founded\": \"18/10/1922\",\n" +
                "  \"country_code\": \"GB\",\n" +
                "  \"vat_number\": \"333289454\",\n" +
                "  \"registration_number\": \"3686147\",\n" +
                "  \"legal_entity_type\": \"NONPROFIT_ORGANIZATION\",\n" +
                "  \"contact_details\": {\n" +
                "    \"phone_number\": \"+443700100222\",\n" +
                "    \"fax\": \"\",\n" +
                "    \"email\": \"yourquestions@bbc.co.uk\"\n" +
                "  }\n" +
                "}"
    }

    fun orgRequestJsonCountryCodeBlank(): String {
        return "{\n" +
                "  \"name\": \"BBC\",\n" +
                "  \"date_founded\": \"18/10/1922\",\n" +
                "  \"country_code\": \"\",\n" +
                "  \"vat_number\": \"333289454\",\n" +
                "  \"registration_number\": \"3686147\",\n" +
                "  \"legal_entity_type\": \"NONPROFIT_ORGANIZATION\",\n" +
                "  \"contact_details\": {\n" +
                "    \"phone_number\": \"+443700100222\",\n" +
                "    \"fax\": \"\",\n" +
                "    \"email\": \"yourquestions@bbc.co.uk\"\n" +
                "  }\n" +
                "}"
    }

    fun orgRequestJsonNoCountryCode(): String {
        return "{\n" +
                "  \"name\": \"BBC\",\n" +
                "  \"date_founded\": \"18/10/1922\",\n" +
                "  \"vat_number\": \"333289454\",\n" +
                "  \"registration_number\": \"3686147\",\n" +
                "  \"legal_entity_type\": \"NONPROFIT_ORGANIZATION\",\n" +
                "  \"contact_details\": {\n" +
                "    \"phone_number\": \"+443700100222\",\n" +
                "    \"fax\": \"\",\n" +
                "    \"email\": \"yourquestions@bbc.co.uk\"\n" +
                "  }\n" +
                "}"
    }

    fun orgRequestJsonCountryCodeIncorrect(): String {
        return "{\n" +
                "  \"name\": \"BBC\",\n" +
                "  \"date_founded\": \"18/10/1922\",\n" +
                "  \"country_code\": \"XX\",\n" +
                "  \"vat_number\": \"333289454\",\n" +
                "  \"registration_number\": \"3686147\",\n" +
                "  \"legal_entity_type\": \"NONPROFIT_ORGANIZATION\",\n" +
                "  \"contact_details\": {\n" +
                "    \"phone_number\": \"+443700100222\",\n" +
                "    \"fax\": \"\",\n" +
                "    \"email\": \"yourquestions@bbc.co.uk\"\n" +
                "  }\n" +
                "}"
    }

    fun orderRequestJsonOk(orgId: String): String {
        return "{\n" +
                "    \"organisation_id\": \"${orgId}\",\n" +
                "    \"order_amount\": 100.00,\n" +
                "    \"order_date\": \"20/11/2023\"\n" +
                "}"
    }

    fun shipmentRequestJsonOk(orderId: String, amount: String): String {
        return "{\n" +
                "    \"order_id\": \"${orderId}\",\n" +
                "    \"amount\": ${amount},\n" +
                "    \"shipment_date\": \"20/11/2023\"\n" +
                "}"
    }

    fun bbcFixture(id: UUID): Map<String, Any> {
        val data = HashMap<String, Any>()
        data["id"] = id
        data["name"] = "BBC"
        data["date_founded"] = SimpleDateFormat("yyyy-MM-dd").parse("1922-10-18")
        data["country_code"] = "GB"
        data["vat_number"] = "333289454"
        data["registration_number"] = "3686147"
        data["legal_entity_type"] = "NONPROFIT_ORGANIZATION"
        return data
    }

    fun bbcContactFixture(id: UUID): Map<String, Any> {
        val data = HashMap<String, Any>()
        data["id"] = id
        data["phone_number"] = "+443700100222"
        data["fax"] = ""
        data["email"] = "yourquestions@bbc.co.uk"
        return data
    }

    fun bbcOrderFixture(orderId: UUID, orgId: UUID): Map<String, Any> {
        val data = HashMap<String, Any>()
        data["id"] = orderId
        data["order_date"] = SimpleDateFormat("yyyy-MM-dd").parse("2023-11-20")
        data["total_amount"] = BigDecimal("100.00")
        data["organisation_id"] = orgId
        return data
    }

}

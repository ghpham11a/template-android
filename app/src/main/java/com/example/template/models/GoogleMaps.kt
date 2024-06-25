package com.example.template.models

data class AddressComponent(
    val long_name: String,
    val short_name: String,
    val types: List<String>
)

data class Location(
    val lat: Double,
    val lng: Double
)

data class Viewport(
    val northeast: Location,
    val southwest: Location
)

data class Geometry(
    val location: Location,
    val location_type: String,
    val viewport: Viewport
)

data class PlusCode(
    val compound_code: String,
    val global_code: String
)

data class Result(
    val address_components: List<AddressComponent>,
    val formatted_address: String,
    val geometry: Geometry,
    val place_id: String,
    val plus_code: PlusCode,
    val types: List<String>
)

data class GoogleGeocodeResponse(
    val results: List<Result>,
    val status: String
)
package com.example.radiusassignment.data.model.networkmodels

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class FacilitiesDataDataModel(
    @Json(name = "facility_id") val facilityID: String,
    @Json(name = "name") val name: String,
    @Json(name = "options") val options: List<OptionsDataDataModel>
)

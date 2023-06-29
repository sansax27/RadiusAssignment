package com.example.radiusassignment.data.model.networkmodels

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class FacilityExclusionDataDataModel(
    @Json(name = "facility_id") val facilityID: String,
    @Json(name = "options_id") val optionsID: String
)
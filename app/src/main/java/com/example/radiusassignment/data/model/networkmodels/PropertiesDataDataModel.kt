package com.example.radiusassignment.data.model.networkmodels

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class PropertiesDataDataModel(
    @Json(name = "facilities") val facilities: List<FacilitiesDataDataModel>,
    @Json(name = "exclusions") val exclusions: List<List<FacilityExclusionDataDataModel>>
)

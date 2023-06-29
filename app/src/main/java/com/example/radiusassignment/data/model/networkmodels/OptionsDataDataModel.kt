package com.example.radiusassignment.data.model.networkmodels

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class OptionsDataDataModel(
    @Json(name = "name") val name: String,
    @Json(name = "icon") val icon: String,
    @Json(name = "id") val id: String
)

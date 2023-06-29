package com.example.radiusassignment.data.model.databasemodels

import androidx.room.ColumnInfo


data class FacilityDataDataModel(
    @ColumnInfo(name = "facility_id") val facilityID: String,
    @ColumnInfo(name = "facility_name") val facilityName: String,
    @ColumnInfo(name = "options_id") val optionsID: String,
    @ColumnInfo(name = "options_name") val optionsName: String,
    @ColumnInfo(name = "options_icon") val optionsIcon: String
)

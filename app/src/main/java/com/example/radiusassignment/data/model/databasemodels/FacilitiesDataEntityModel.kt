package com.example.radiusassignment.data.model.databasemodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "facilities")
data class FacilitiesDataEntityModel(
    @PrimaryKey
    @ColumnInfo(name = "facility_id") val facilityID : String,
    @ColumnInfo(name = "name") val name: String
)

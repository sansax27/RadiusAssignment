package com.example.radiusassignment.data.model.databasemodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(foreignKeys = [ForeignKey(entity = FacilitiesDataEntityModel::class,
    parentColumns = arrayOf("facility_id"),
    childColumns = arrayOf("facility_id"),
    onDelete = ForeignKey.CASCADE
)], tableName = "options")
data class OptionsDataEntityModel(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "icon") val icon: String,
    @ColumnInfo(name = "facility_id") val facilityID: String
)

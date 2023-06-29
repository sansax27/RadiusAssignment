package com.example.radiusassignment.data.model.databasemodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(foreignKeys = [ForeignKey(entity = FacilitiesDataEntityModel::class,
    parentColumns = arrayOf("facility_id"),
    childColumns = arrayOf("facility_id"),
    onDelete = ForeignKey.CASCADE
), ForeignKey(entity = OptionsDataEntityModel::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("options_id"),
    onDelete = ForeignKey.CASCADE
)], tableName = "exclusions")
data class ExclusionsDataEntityModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "group_no") val groupNo: Int,
    @ColumnInfo(name = "facility_id") val facilityID : String,
    @ColumnInfo(name = "options_id") val optionsID: String
)

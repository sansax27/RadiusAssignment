package com.example.radiusassignment.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.radiusassignment.data.dao.ExclusionsDao
import com.example.radiusassignment.data.dao.FacilitiesDao
import com.example.radiusassignment.data.dao.OptionsDao
import com.example.radiusassignment.data.model.databasemodels.ExclusionsDataEntityModel
import com.example.radiusassignment.data.model.databasemodels.FacilitiesDataEntityModel
import com.example.radiusassignment.data.model.databasemodels.OptionsDataEntityModel


@Database(entities = [ExclusionsDataEntityModel::class, FacilitiesDataEntityModel::class, OptionsDataEntityModel::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun exclusionsDao(): ExclusionsDao
    abstract fun facilitiesDao(): FacilitiesDao
    abstract fun optionsDao(): OptionsDao
}
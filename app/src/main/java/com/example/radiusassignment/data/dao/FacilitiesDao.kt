package com.example.radiusassignment.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.radiusassignment.data.model.databasemodels.FacilitiesDataEntityModel
import kotlinx.coroutines.flow.Flow


@Dao
interface FacilitiesDao {


    @Insert
    fun insertFacilities(facilities: List<FacilitiesDataEntityModel>)
}
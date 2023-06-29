package com.example.radiusassignment.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.radiusassignment.data.model.databasemodels.ExclusionsDataEntityModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ExclusionsDao {

    @Query("select * from exclusions")
    fun getExclusions(): Flow<List<ExclusionsDataEntityModel>>

    @Insert
    fun insertExclusions(exclusions: List<ExclusionsDataEntityModel>)

}
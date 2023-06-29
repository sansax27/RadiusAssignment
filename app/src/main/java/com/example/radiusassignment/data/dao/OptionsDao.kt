package com.example.radiusassignment.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.radiusassignment.data.model.databasemodels.FacilityDataDataModel
import com.example.radiusassignment.data.model.databasemodels.OptionsDataEntityModel
import kotlinx.coroutines.flow.Flow


@Dao
interface OptionsDao {

    @Insert
    fun insertOptions(options: List<OptionsDataEntityModel>)

    @Query("select op.id as options_id, op.name as options_name, op.icon as options_icon, fe.facility_id as facility_id, fe.name as facility_name from facilities fe inner join options op on fe.facility_id=op.facility_id")
    fun getFacilityOptionsData(): Flow<List<FacilityDataDataModel>>


}
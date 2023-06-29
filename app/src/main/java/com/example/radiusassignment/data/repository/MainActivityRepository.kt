package com.example.radiusassignment.data.repository

import com.example.radiusassignment.data.ApiService
import com.example.radiusassignment.data.AppDatabase
import com.example.radiusassignment.data.NetworkResult
import com.example.radiusassignment.data.NetworkUtils
import com.example.radiusassignment.data.dao.ExclusionsDao
import com.example.radiusassignment.data.dao.FacilitiesDao
import com.example.radiusassignment.data.dao.OptionsDao
import com.example.radiusassignment.data.model.databasemodels.ExclusionsDataEntityModel
import com.example.radiusassignment.data.model.databasemodels.FacilitiesDataEntityModel
import com.example.radiusassignment.data.model.databasemodels.OptionsDataEntityModel
import com.example.radiusassignment.data.model.networkmodels.PropertiesDataDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MainActivityRepository @Inject constructor(private val facilitiesDao: FacilitiesDao,
                                                 private val exclusionsDao: ExclusionsDao,
                                                 private val apiService: ApiService,
                                                 private val roomDatabase: AppDatabase,
                                                 private val optionsDao: OptionsDao) {

    val facilityDataFlow = optionsDao.getFacilityOptionsData()
    val exclusionsDataFlow = exclusionsDao.getExclusions()

    suspend fun getFacilityData() : NetworkResult<PropertiesDataDataModel> = withContext(Dispatchers.IO) {
        NetworkUtils.callApi { apiService.getPropertiesData() }
    }

    suspend fun syncDBData(data: PropertiesDataDataModel) = roomDatabase.runInTransaction {
        roomDatabase.clearAllTables()
        val facilitiesList = mutableListOf<FacilitiesDataEntityModel>()
        val optionsList = mutableListOf<OptionsDataEntityModel>()
        for (facility in data.facilities) {
            facilitiesList.add(
                FacilitiesDataEntityModel(
                    facility.facilityID,
                    facility.name
                )
            )
            for (option in facility.options) {
                optionsList.add(
                    OptionsDataEntityModel(
                        option.id,
                        option.name,
                        option.icon,
                        facility.facilityID
                    )
                )
            }
        }
        facilitiesDao.insertFacilities(facilitiesList)
        optionsDao.insertOptions(optionsList)
        val exclusionsList = mutableListOf<ExclusionsDataEntityModel>()
        var groupNo = 1
        for (exclusions in data.exclusions) {
            for (exclusion in exclusions) {
                exclusionsList.add(
                    ExclusionsDataEntityModel(
                        groupNo = groupNo,
                        facilityID = exclusion.facilityID,
                        optionsID = exclusion.optionsID
                    )
                )
            }
            groupNo++
        }
        exclusionsDao.insertExclusions(exclusionsList)
    }

}
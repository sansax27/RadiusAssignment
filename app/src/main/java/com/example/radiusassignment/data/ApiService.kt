package com.example.radiusassignment.data

import com.example.radiusassignment.data.model.networkmodels.PropertiesDataDataModel
import retrofit2.Response
import retrofit2.http.GET
import javax.inject.Singleton


@Singleton
interface ApiService {

    @GET(".")
    suspend fun getPropertiesData(): Response<PropertiesDataDataModel>

}
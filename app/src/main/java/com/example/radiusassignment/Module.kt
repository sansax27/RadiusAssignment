package com.example.radiusassignment

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.radiusassignment.data.ApiService
import com.example.radiusassignment.data.AppDatabase
import com.example.radiusassignment.data.dao.ExclusionsDao
import com.example.radiusassignment.data.dao.FacilitiesDao
import com.example.radiusassignment.data.dao.OptionsDao
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.addAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {


    @Provides
    @Singleton
    fun getOptionsDao(roomDB: AppDatabase): OptionsDao = roomDB.optionsDao()

    @Provides
    @Singleton
    fun getFacilitiesDao(roomDB: AppDatabase): FacilitiesDao = roomDB.facilitiesDao()

    @Provides
    @Singleton
    fun getExclusionsDao(roomDB: AppDatabase): ExclusionsDao = roomDB.exclusionsDao()

    @Provides
    @Singleton
    fun getRoomDB(@ApplicationContext applicationContext: Context) : AppDatabase = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app-db").build()

    @Provides
    @Singleton
    fun getApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @OptIn(ExperimentalStdlibApi::class)
    @Provides
    @Singleton
    fun getRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder().client(client).baseUrl(
        Constants.BASE_URL
    ).addConverterFactory(
        MoshiConverterFactory.create(Moshi.Builder().add(KotlinJsonAdapterFactory())
            .addAdapter(Moshi.Builder().build().adapter<Map<String, Int>>(Types.newParameterizedType(Map::class.java, String::class.java, Int::class.javaObjectType))).build()))
        .build()

    @Provides
    @Singleton
    fun getRetrofitClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }).build()


}
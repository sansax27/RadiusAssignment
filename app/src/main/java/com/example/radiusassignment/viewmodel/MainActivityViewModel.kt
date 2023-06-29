package com.example.radiusassignment.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.radiusassignment.Constants
import com.example.radiusassignment.R
import com.example.radiusassignment.data.NetworkResult
import com.example.radiusassignment.data.UIStatus
import com.example.radiusassignment.data.repository.MainActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class MainActivityViewModel @Inject constructor(private val mainActivityRepository: MainActivityRepository,
                                                @ApplicationContext context: Context): ViewModel() {

    val facilityDataFlow = mainActivityRepository.facilityDataFlow
    val exclusionDataFlow = mainActivityRepository.exclusionsDataFlow

    private val apiSyncStatusPrivate = MutableStateFlow<UIStatus<String>>(UIStatus.Idle)
    val apiSyncStatus : StateFlow<UIStatus<String>> = apiSyncStatusPrivate


    init {
        val lastTimeSync = context.getSharedPreferences(Constants.APP_SHARED_PREFERENCES, Context.MODE_PRIVATE).getLong(Constants.LAST_TIME_SYNC, -1)
        if (lastTimeSync==-1L || (TimeUnit.MILLISECONDS.toDays(Date().time - lastTimeSync)>=1)) {
            viewModelScope.launch(Dispatchers.IO) {
                apiSyncStatusPrivate.emit(UIStatus.Loading)
                when(val response = mainActivityRepository.getFacilityData()) {
                    is NetworkResult.Success -> {
                        mainActivityRepository.syncDBData(response.data)
                        context.getSharedPreferences(Constants.APP_SHARED_PREFERENCES, Context.MODE_PRIVATE).edit().putLong(Constants.LAST_TIME_SYNC, Date().time).apply()
                        apiSyncStatusPrivate.emit(UIStatus.Success(""))
                    }
                    is NetworkResult.Failure -> apiSyncStatusPrivate.emit(UIStatus.Error("${response.code} ${response.message}"))
                    is NetworkResult.Exception -> {
                        when(response.e) {
                            is IOException -> apiSyncStatusPrivate.emit(UIStatus.Error(context.getString(R.string.internet_issue_string)))
                            is HttpException -> apiSyncStatusPrivate.emit(UIStatus.Error(context.getString(R.string.cannot_reach_server_string)))
                            else -> apiSyncStatusPrivate.emit(UIStatus.Error(response.e.message ?: ""))
                        }

                    }
                }
            }
        }
    }

}
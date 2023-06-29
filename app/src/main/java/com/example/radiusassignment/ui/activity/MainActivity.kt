package com.example.radiusassignment.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.radiusassignment.R
import com.example.radiusassignment.adapter.rv.FacilitiesRVAdapter
import com.example.radiusassignment.data.UIStatus
import com.example.radiusassignment.data.model.networkmodels.FacilityExclusionDataDataModel
import com.example.radiusassignment.data.model.networkmodels.OptionsDataDataModel
import com.example.radiusassignment.databinding.ActivityMainBinding
import com.example.radiusassignment.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainActivityViewModel>()

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val facilitiesNameList = mutableListOf<String>()
    private val facilitiesRVAdapter = FacilitiesRVAdapter {
        try {
            binding.facilityTypeTv.text =
                getString(R.string.select_facility_type_string, facilitiesNameList[it-1])
        } catch (e: Exception) {
            binding.facilityTypeTv.text = getString(R.string.select_facility_type_def_string)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.facilityDataRv.layoutManager = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        binding.facilityDataRv.adapter = facilitiesRVAdapter
        viewModel.viewModelScope.launch {
            viewModel.facilityDataFlow.collect { facilityDataModel ->
                val facilities = mutableMapOf<String, Pair<String, MutableList<OptionsDataDataModel>>>()
                for (facility in facilityDataModel) {
                    if (facilities[facility.facilityID]==null) {
                        facilities[facility.facilityID] = Pair(facility.facilityName, mutableListOf(OptionsDataDataModel(facility.optionsName, facility.optionsIcon, facility.optionsID)))
                    } else {
                        facilities[facility.facilityID]!!.second.add(OptionsDataDataModel(facility.optionsName, facility.optionsIcon, facility.optionsID))
                    }
                }
                val facilitiesSortedMap = facilities.toSortedMap()
                facilitiesNameList.clear()
                facilitiesNameList.addAll(facilitiesSortedMap.values.map { it.first })
                val facilityOptionsMap = facilitiesSortedMap.values.map { it.second }
                facilitiesRVAdapter.updateFacilityDataList(facilityOptionsMap)
                if (facilityOptionsMap.isEmpty()) {
                    if (viewModel.apiSyncStatus.value==UIStatus.Loading) {
                        binding.facilityPb.visibility = View.VISIBLE
                        binding.facilityDataRv.visibility = View.GONE
                    }
                } else {
                    binding.facilityDataRv.visibility = View.VISIBLE
                    binding.facilityPb.visibility = View.GONE
                }
            }
        }
        viewModel.viewModelScope.launch {
            viewModel.apiSyncStatus.collect {
                when(it) {
                    is UIStatus.Error -> {
                        binding.root.visibility = View.GONE
                        Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_LONG).show()
                    }
                    else -> {}
                }
            }
        }
        viewModel.viewModelScope.launch {
            viewModel.exclusionDataFlow.collect {
                val exclusionDataListMap = mutableMapOf<Int, MutableList<FacilityExclusionDataDataModel>>()
                for (exclusion in it) {
                    if (exclusionDataListMap[exclusion.groupNo]==null) {
                        exclusionDataListMap[exclusion.groupNo] = mutableListOf(FacilityExclusionDataDataModel(exclusion.facilityID, exclusion.optionsID))
                    } else {
                        exclusionDataListMap[exclusion.groupNo]!!.add(FacilityExclusionDataDataModel(exclusion.facilityID, exclusion.optionsID))
                    }
                }
                facilitiesRVAdapter.updateExclusions(exclusionDataListMap.values.map { exclusionList -> exclusionList })
            }
        }
        binding.facilityDataRv.layoutManager = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        onBackPressedDispatcher.addCallback(this) {
            val backResponse = facilitiesRVAdapter.goBack()
            if (!backResponse) {
                finish()
            }
        }
        setContentView(binding.root)
    }

}
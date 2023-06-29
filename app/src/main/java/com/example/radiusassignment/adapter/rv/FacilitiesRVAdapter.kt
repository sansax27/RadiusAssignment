package com.example.radiusassignment.adapter.rv

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.radiusassignment.R
import com.example.radiusassignment.Utils
import com.example.radiusassignment.data.model.networkmodels.FacilityExclusionDataDataModel
import com.example.radiusassignment.data.model.networkmodels.OptionsDataDataModel
import com.example.radiusassignment.databinding.FacilityRvItemBinding
import timber.log.Timber

class FacilitiesRVAdapter(private val onPositionChanged: (position: Int) -> Unit):
    RecyclerView.Adapter<FacilitiesRVAdapter.FacilityViewHolder>() {

    private val facilityDataList = mutableListOf<List<OptionsDataDataModel>>()
    private val facilityData = mutableListOf<OptionsDataDataModel>()
    private val exclusionDataList = mutableListOf<List<FacilityExclusionDataDataModel>>()
    private var currentPosition = 1
    private val facilitySelectedIDMap = mutableMapOf<Int, String>()

    @SuppressLint("NotifyDataSetChanged")
    fun updatePosition(newPosition: Int): Boolean {
        this.currentPosition = newPosition
        this.facilityData.clear()
        this.facilityData.addAll(facilityDataList[currentPosition-1])
        for (exclusions in exclusionDataList) {
            val exclusionIDs =
                exclusions.associate { exclusion -> exclusion.facilityID to exclusion.optionsID }
                    .toSortedMap().toMutableMap()
            if (exclusionIDs.keys.contains(currentPosition.toString())) {
                val removeOptionID = exclusionIDs.remove(currentPosition.toString())!!
                for (exclusion in exclusionIDs) {
                    Timber.e("${exclusion.key} ${exclusion.value}")
                    if (exclusion.key.toInt()>currentPosition) {
                        break
                    }
                    if (exclusion.value == facilitySelectedIDMap[exclusion.key.toInt()]) {
                        this.facilityData.retainAll { optionsDataDataModel -> optionsDataDataModel.id != removeOptionID }
                        break
                    }

                }
            }
        }
        if (this.facilityData.isEmpty()) {
            currentPosition--
            updatePosition(currentPosition)
            onPositionChanged(currentPosition)
            return false
        }
        onPositionChanged(currentPosition)
        notifyDataSetChanged()
        return true
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateExclusions(exclusionDataList : List<List<FacilityExclusionDataDataModel>>) {
        this.exclusionDataList.clear()
        this.exclusionDataList.addAll(exclusionDataList)
        currentPosition = 1
        if (facilityDataList.isNotEmpty()) {
            updatePosition(currentPosition)
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateFacilityDataList(facilityDataList: List<List<OptionsDataDataModel>>) {
        if (facilityDataList.isNotEmpty()) {
            this.facilityDataList.clear()
            this.facilityDataList.addAll(facilityDataList)
            currentPosition = 1
            updatePosition(currentPosition)
        }
    }

    fun goBack(): Boolean {
        return if (currentPosition != 1) {
            currentPosition--
            updatePosition(currentPosition)
            true
        } else {
            false
        }
    }


    inner class FacilityViewHolder(binding: FacilityRvItemBinding): RecyclerView.ViewHolder(binding.root) {
        val facilityIV = binding.facilityIv
        val facilityNameTV = binding.facilityNameTv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacilityViewHolder {
        return FacilityViewHolder(FacilityRvItemBinding.inflate(LayoutInflater.from(parent.context),
            parent, false))
    }

    override fun getItemCount(): Int {
        return facilityData.size
    }

    override fun onBindViewHolder(holder: FacilityViewHolder, position: Int) {
        holder.apply {
            facilityNameTV.text = facilityData[position].name
            try {
                facilityIV.setImageDrawable(ContextCompat.getDrawable(itemView.context,
                    Utils.getResId(facilityData[position].icon.replace("-","_"), R.drawable::class.java)))
            } catch (e: Exception) {
                facilityIV.setImageDrawable(ContextCompat.getDrawable(itemView.context,
                R.drawable.placeholder_image_icon))
            }

            holder.itemView.setOnClickListener {
                facilitySelectedIDMap[currentPosition] = facilityData[position].id
                Timber.e(facilitySelectedIDMap.toString())
                if ((currentPosition + 1)<=facilityDataList.size) {
                    currentPosition++
                    val updatePositionResult = updatePosition(currentPosition)
                    if (!updatePositionResult) {
                        Toast.makeText(itemView.context, itemView.resources.getString(R.string.cannot_select_more_with_previous_combination_string), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
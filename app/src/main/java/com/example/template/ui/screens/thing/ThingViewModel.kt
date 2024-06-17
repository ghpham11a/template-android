package com.example.template.ui.screens.thing

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.models.Thing
import com.example.template.models.ThingType
import com.example.template.repositories.UserRepository
import com.example.template.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThingViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _pageCount = MutableStateFlow(1)
    val pageCount: StateFlow<Int> = _pageCount

    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage

    private val _isNextEnabled = MutableStateFlow(false)
    val isNextEnabled: StateFlow<Boolean> = _isNextEnabled

    private val _isSaveEnabled = MutableStateFlow(false)
    val isSaveEnabled: StateFlow<Boolean> = _isSaveEnabled

    val _availableThingTypes = MutableStateFlow<List<ThingType>>(emptyList())
    val availableThingTypes: StateFlow<List<ThingType>> = _availableThingTypes

    private val _thing = MutableStateFlow(Thing())
    val thing: StateFlow<Thing> = _thing

    init {
        _availableThingTypes.value = listOf(
            ThingType("GROUP 1", "Type 1", "Type 1 desciption"),
            ThingType("GROUP 1", "Type 2", "Type 2 desciption"),
            ThingType("GROUP 2", "Type 3", "Type 3 desciption"),
            ThingType("GROUP 2", "Type 4", "Type 4 desciption"),
        )
    }

    fun updatePageCount(count: Int) {
        _pageCount.value = count
    }

    fun updateCurrentPage(page: Int) {
        _currentPage.value = page
    }

    fun selectThingType(thingType: ThingType) {
        _thing.value = _thing.value.copy(thingType = thingType)
        validateThing(Constants.ThingScreen.THING_TYPE)
    }

    fun onDescriptionChange(description: String) {
        _thing.value = _thing.value.copy(thingDescription = description)
        validateThing(Constants.ThingScreen.THING_DESCRIPTION)
    }

    fun validateThing(screen: String) {
        when (screen) {
            Constants.ThingScreen.THING_TYPE -> {
                val thingTypeNotEmpty = _thing.value.thingType != null
                _isNextEnabled.value = thingTypeNotEmpty
                _isSaveEnabled.value = thingTypeNotEmpty && (currentPage.value == pageCount.value - 1)

            }
            Constants.ThingScreen.THING_DESCRIPTION -> {
                val thingDescriptionNotEmpty = _thing.value.thingDescription != null
                val thingDescriptionNotBlank = _thing.value.thingDescription?.length != 0
                _isNextEnabled.value = thingDescriptionNotEmpty && thingDescriptionNotBlank
                _isSaveEnabled.value = (thingDescriptionNotEmpty && thingDescriptionNotBlank) && (currentPage.value == pageCount.value - 1)
            }
            Constants.ThingScreen.THING_METHODS -> {
                // _isNextEnabled.value = _thing.value.thingMethods != null
            }
            Constants.ThingScreen.THING_LOCATION -> {
                // _isNextEnabled.value = _thing.value.thingLocation != null
            }
            Constants.ThingScreen.THING_TIME -> {
                // _isNextEnabled.value = _thing.value.thingTime != null
            }
        }
    }

}
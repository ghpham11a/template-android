package com.example.template.ui.screens.availability

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.models.Block
import com.example.template.models.ProxyCallEvent
import com.example.template.repositories.UserRepository
import com.example.template.utils.toLocalDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AvailabilityScreenViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _displayedBlocks = MutableStateFlow<List<Block>>(emptyList())
    val displayedBlocks: StateFlow<List<Block>> = _displayedBlocks

    private val _availabilityType = MutableStateFlow("1")
    val availabilityType: StateFlow<String> = _availabilityType


    private val _selectedDay = MutableStateFlow(LocalDate.now())
    val selectedDay: StateFlow<LocalDate> = _selectedDay

    private val _days = MutableStateFlow(emptyList<LocalDate>())
    val days: StateFlow<List<LocalDate>> = _days

    val availabilityType1Blocks: MutableList<Block> = mutableListOf<Block>()
    val availabilityType2Blocks: MutableList<Block> = mutableListOf<Block>()
    val availabilityType3Blocks: MutableList<Block> = mutableListOf<Block>()
    val availabilityType4Blocks: MutableList<Block> = mutableListOf<Block>()

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    init {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _days.value = getDatesInRange().map { LocalDate.parse(it) }
            }
        }
    }

    fun updateSelectedDay(day: LocalDate) {
        _selectedDay.value = day
        updateDisplayedBlocks(day, availabilityType.value)
    }

    fun addBlock() {

        val key = _selectedDay.value.format(formatter)

        when (availabilityType.value) {
            "1" -> availabilityType1Blocks.add(Block(key, "2024-07-21 13:30".toLocalDateTime(), "2024-07-21 17:00".toLocalDateTime()))
            "2" -> availabilityType2Blocks.add(Block(key, "2024-07-21 13:30".toLocalDateTime(), "2024-07-21 17:00".toLocalDateTime()))
            "3" -> availabilityType3Blocks.add(Block(key, "2024-07-21 13:30".toLocalDateTime(), "2024-07-21 17:00".toLocalDateTime()))
            "4" -> availabilityType4Blocks.add(Block(key, "2024-07-21 13:30".toLocalDateTime(), "2024-07-21 17:00".toLocalDateTime()))
        }

        updateDisplayedBlocks(selectedDay.value, availabilityType.value)
    }

    fun updateDisplayedBlocks(selectedDay: LocalDate, availabilityType: String) {
        val key = _selectedDay.value.format(formatter)
        when (availabilityType) {
            "1" -> _displayedBlocks.value = availabilityType1Blocks.filter { it.key == key }
            "2" -> _displayedBlocks.value = availabilityType2Blocks.filter { it.key == key }
            "3" -> _displayedBlocks.value = availabilityType3Blocks.filter { it.key == key }
            "4" -> _displayedBlocks.value = availabilityType4Blocks.filter { it.key == key }
        }
    }

    fun updateAvailabilityType(value: String) {
        _availabilityType.value = value.toString()
        updateDisplayedBlocks(_selectedDay.value, value)
    }

    fun getDatesInRange(): List<String> {
        // Get the current date
        val currentDate = Date()

        // Calculate the start and end dates
        val calendar = Calendar.getInstance()
        calendar.time = currentDate
        calendar.add(Calendar.DAY_OF_YEAR, -2)
        val twoDaysAgo = calendar.time
        calendar.time = currentDate
        calendar.add(Calendar.DAY_OF_YEAR, 14)
        val twoWeeksLater = calendar.time

        // Date formatter to display the dates in the desired format
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Initialize the date to the start date
        calendar.time = twoDaysAgo

        // List to hold the formatted date strings
        val dateStrings = mutableListOf<String>()

        // Loop through each date in the range
        while (!calendar.time.after(twoWeeksLater)) {
            // Convert the date to a string
            val dateString = dateFormatter.format(calendar.time)

            // Append the date string to the list
            dateStrings.add(dateString)

            // Move to the next date
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        return dateStrings
    }

}
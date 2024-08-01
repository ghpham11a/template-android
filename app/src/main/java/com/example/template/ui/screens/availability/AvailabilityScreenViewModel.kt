package com.example.template.ui.screens.availability

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.models.Block
import com.example.template.models.UpdateAvailability
import com.example.template.models.UpdateUserBody
import com.example.template.networking.APIGateway
import com.example.template.repositories.UserRepository
import com.example.template.utils.toHourMinuteString
import com.example.template.utils.toLocalDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AvailabilityScreenViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _displayedBlocks = MutableStateFlow<List<Block>>(emptyList())
    val displayedBlocks: StateFlow<List<Block>> = _displayedBlocks

    private val _availabilityType = MutableStateFlow("1")
    val availabilityType: StateFlow<String> = _availabilityType

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _selectedDay = MutableStateFlow(LocalDate.now())
    val selectedDay: StateFlow<LocalDate> = _selectedDay

    private val _days = MutableStateFlow(emptyList<LocalDate>())
    val days: StateFlow<List<LocalDate>> = _days

    private var availabilityType1Blocks: MutableList<Block> = mutableListOf<Block>()
    private var availabilityType2Blocks: MutableList<Block> = mutableListOf<Block>()
    private var availabilityType3Blocks: MutableList<Block> = mutableListOf<Block>()
    private var availabilityType4Blocks: MutableList<Block> = mutableListOf<Block>()

    private val dayFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    var selectedId: String = ""
    var selectedTimeType: String = ""

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

    private fun findBlocksAndIndex(): Pair<List<Block>, Int> {
        val key = _selectedDay.value.format(dayFormatter)
        var typeBlocks = when (availabilityType.value) {
            "1" -> availabilityType1Blocks
            "2" -> availabilityType2Blocks
            "3" -> availabilityType3Blocks
            "4" -> availabilityType4Blocks
            else -> emptyList()
        }
        typeBlocks = typeBlocks.filter { it.key == key }
        if (typeBlocks.isEmpty()) return Pair(emptyList(), -1)
        val targetBlock = typeBlocks.first { it.id == selectedId }
        val blockIndex = typeBlocks.indexOf(targetBlock)
        return Pair(typeBlocks, blockIndex)
    }

    fun tapStart(): List<String> {

        val (typeBlocks, blockIndex) = findBlocksAndIndex()

        return when {
            typeBlocks.size == 1 || blockIndex == (typeBlocks.size - 1) -> {
                val endTime = typeBlocks[blockIndex].endTime.toHourMinuteString()
                createRange(end = endTime)
            }
            typeBlocks.size == 2 && blockIndex == 0 -> {
                val endTime = typeBlocks[blockIndex].endTime.toHourMinuteString()
                createRange(end = endTime)
            }
            typeBlocks.size == 2 && blockIndex == 1 -> {
                val startTime = typeBlocks[0].endTime.toHourMinuteString()
                val endTime = typeBlocks[blockIndex].endTime.toHourMinuteString()
                createRange(start = startTime, end = endTime)
            }
            else -> {
                val startTime = typeBlocks[blockIndex - 1].endTime.toHourMinuteString()
                val endTime = typeBlocks[blockIndex].endTime.toHourMinuteString()
                createRange(start = startTime, end = endTime)
            }
        }
    }

    fun tapEnd(): List<String> {
        val (typeBlocks, blockIndex) = findBlocksAndIndex()

        return when {
            typeBlocks.size == 1 || blockIndex == (typeBlocks.size - 1) -> {
                val startTime = typeBlocks[blockIndex].startTime.toHourMinuteString()
                createRange(start = startTime)
            }
            typeBlocks.size == 2 && blockIndex == 0 -> {
                val startTime = typeBlocks[blockIndex].startTime.toHourMinuteString()
                val endTime = typeBlocks[1].startTime.toHourMinuteString()
                createRange(start = startTime, end = endTime)
            }
            typeBlocks.size == 2 && blockIndex == 1 -> {
                val startTime = typeBlocks[blockIndex].startTime.toHourMinuteString()
                createRange(start = startTime)
            }
            else -> {
                val startTime = typeBlocks[blockIndex].startTime.toHourMinuteString()
                val endTime = typeBlocks[blockIndex + 1].startTime.toHourMinuteString()
                createRange(start = startTime, end = endTime)
            }
        }
    }


    private fun createRange(start: String = "00:00", end: String = "23:45"): List<String> {

        val key = _selectedDay.value.format(dayFormatter)

        var startTime = "$key $start".toLocalDateTime()
        val endTime = "$key $end".toLocalDateTime()
        val timeRange = mutableListOf<String>()

        while (!startTime.isAfter(endTime)) {
            timeRange.add(startTime.format(timeFormatter))
            startTime = startTime.plusMinutes(15)
        }

        return timeRange
    }

    fun updateBlockList(blockList: MutableList<Block>, id: String, key: String, time: String, type: String) {
        blockList.indexOfFirst { it.id == id }.let {
            if (type == "start") {
                blockList[it] = Block(id, key, "$key $time".toLocalDateTime(), blockList[it].endTime)
            }
            if (type == "end") {
                blockList[it] = Block(id, key, blockList[it].startTime, "$key $time".toLocalDateTime())
            }
        }
        blockList.sortBy { it.startTime }
    }

    fun updateBlock(id: String, type: String, time: String) {
        val key = _selectedDay.value.format(dayFormatter)
        when (availabilityType.value) {
            "1" -> updateBlockList(availabilityType1Blocks, id, key, time, type)
            "2" -> updateBlockList(availabilityType2Blocks, id, key, time, type)
            "3" -> updateBlockList(availabilityType3Blocks, id, key, time, type)
            "4" -> updateBlockList(availabilityType4Blocks, id, key, time, type)
        }
        _displayedBlocks.value = emptyList()
        viewModelScope.launch {
            delay(500)
            updateDisplayedBlocks(selectedDay.value, availabilityType.value)
        }
    }

    fun setupBlocks() {
        val user = userRepository.userPrivate?.user ?: return

        val keyFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val availabilityTypes = listOf(
            user.availabilityType1 ?: emptyList(),
            user.availabilityType2 ?: emptyList(),
            user.availabilityType3 ?: emptyList(),
            user.availabilityType4 ?: emptyList()
        )

        for ((index, blocks) in availabilityTypes.withIndex()) {
            val realType = index + 1

            for (block in blocks) {
                val startAndEndBlocks = block.split("->")

                val start = startAndEndBlocks[0].toLocalDateTime()
                val end = startAndEndBlocks[1].toLocalDateTime()
                val key = keyFormatter.format(start)

                val newBlock = Block(UUID.randomUUID().toString(), key, start, end)

                when (realType) {
                    1 -> {
                        availabilityType1Blocks.add(newBlock)
                        availabilityType1Blocks.sortBy { it.startTime }
                    }
                    2 -> {
                        availabilityType2Blocks.add(newBlock)
                        availabilityType2Blocks.sortBy { it.startTime }
                    }
                    3 -> {
                        availabilityType3Blocks.add(newBlock)
                        availabilityType3Blocks.sortBy { it.startTime }
                    }
                    4 -> {
                        availabilityType4Blocks.add(newBlock)
                        availabilityType4Blocks.sortBy { it.startTime }
                    }
                }
            }
        }

        updateDisplayedBlocks(selectedDay.value, availabilityType.value)
    }

    fun addBlock() {

        val key = _selectedDay.value.format(dayFormatter)

        val typeBlocks = when (availabilityType.value) {
            "1" -> availabilityType1Blocks.filter { it.key == key }
            "2" -> availabilityType2Blocks.filter { it.key == key }
            "3" -> availabilityType3Blocks.filter { it.key == key }
            "4" -> availabilityType4Blocks.filter { it.key == key }
            else -> emptyList()
        }

        val block = when {
            typeBlocks.isEmpty() -> {
                Block(UUID.randomUUID().toString(), key, "$key 12:00".toLocalDateTime(), "$key 13:00".toLocalDateTime())
            }
            else -> {
                val startTime = typeBlocks.last().endTime
                val endTime = startTime.plusMinutes(60)
                Block(UUID.randomUUID().toString(), key, startTime, endTime)
            }
        }

        when (availabilityType.value) {
            "1" -> availabilityType1Blocks.add(block)
            "2" -> availabilityType2Blocks.add(block)
            "3" -> availabilityType3Blocks.add(block)
            "4" -> availabilityType4Blocks.add(block)
        }

        updateDisplayedBlocks(selectedDay.value, availabilityType.value)
    }

    fun deleteBlock(id: String) {
        when (availabilityType.value) {
            "1" -> {
                availabilityType1Blocks.indexOfFirst { it.id == id }.let {
                    availabilityType1Blocks.removeAt(it)
                }
            }
            "2" -> {
                availabilityType2Blocks.indexOfFirst { it.id == id }.let {
                    availabilityType2Blocks.removeAt(it)
                }
            }
            "3" -> {
                availabilityType3Blocks.indexOfFirst { it.id == id }.let {
                    availabilityType3Blocks.removeAt(it)
                }
            }
            "4" -> {
                availabilityType4Blocks.indexOfFirst { it.id == id }.let {
                    availabilityType4Blocks.removeAt(it)
                }
            }
        }
        _displayedBlocks.value = emptyList()
        viewModelScope.launch {
            delay(500)
            updateDisplayedBlocks(selectedDay.value, availabilityType.value)
        }
    }

    private fun updateDisplayedBlocks(selectedDay: LocalDate, availabilityType: String) {
        val key = _selectedDay.value.format(dayFormatter)
        when (availabilityType) {
            "1" -> _displayedBlocks.value = availabilityType1Blocks.filter { it.key == key }.toList()
            "2" -> _displayedBlocks.value = availabilityType2Blocks.filter { it.key == key }.toList()
            "3" -> _displayedBlocks.value = availabilityType3Blocks.filter { it.key == key }.toList()
            "4" -> _displayedBlocks.value = availabilityType4Blocks.filter { it.key == key }.toList()
        }
    }

    fun updateAvailabilityType(value: String) {
        _availabilityType.value = value.toString()
        updateDisplayedBlocks(_selectedDay.value, value)
    }

    private fun getDatesInRange(): List<String> {
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

    suspend fun updateAvailability(): Boolean {
        _isLoading.value = true
        return try {

            val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.getDefault())

            val body = UpdateUserBody(
                updateAvailability = UpdateAvailability(
                    availabilityType1 = availabilityType1Blocks.map { "${it.startTime.format(dateTimeFormatter)}->${it.endTime.format(dateTimeFormatter)}" },
                    availabilityType2 = availabilityType2Blocks.map { "${it.startTime.format(dateTimeFormatter)}->${it.endTime.format(dateTimeFormatter)}" },
                    availabilityType3 = availabilityType3Blocks.map { "${it.startTime.format(dateTimeFormatter)}->${it.endTime.format(dateTimeFormatter)}" },
                    availabilityType4 = availabilityType4Blocks.map { "${it.startTime.format(dateTimeFormatter)}->${it.endTime.format(dateTimeFormatter)}" },
                )
            )
            executeUpdate(body)
        } catch (e: Exception) {
            // Handle exception
            _isLoading.value = false
            false
        }
    }

    private suspend fun executeUpdate(body: UpdateUserBody): Boolean {
        val response = APIGateway.api.privateUpdateUser(
            APIGateway.buildAuthorizedHeaders(userRepository.idToken ?: ""),
            userRepository.userId ?: "",
            body
        )
        _isLoading.value = false
        return response.isSuccessful
    }

}
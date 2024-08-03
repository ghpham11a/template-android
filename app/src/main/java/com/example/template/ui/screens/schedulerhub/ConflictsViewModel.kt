package com.example.template.ui.screens.schedulerhub

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.models.Block
import com.example.template.networking.APIGateway
import com.example.template.repositories.SchedulesRepository
import com.example.template.repositories.UserRepository
import com.example.template.ui.screens.schedulerhub.models.DATE_FORMAT
import com.example.template.ui.screens.schedulerhub.models.StartTimeBlock
import com.example.template.ui.screens.schedulerhub.models.StartTimeBlockSection
import com.example.template.utils.toDate
import com.example.template.utils.toLocalDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import java.util.*

@HiltViewModel
class ConflictsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val schedulesRepository: SchedulesRepository
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _durationOptions = MutableStateFlow(emptyList<Int>())
    val durationOptions: StateFlow<List<Int>> = _durationOptions

    private val _duration = MutableStateFlow(0)
    val duration: StateFlow<Int> = _duration

    private val _timeOptions = MutableStateFlow(emptyList<StartTimeBlockSection>())
    val timeOptions: StateFlow<List<StartTimeBlockSection>> = _timeOptions

    private val _time = MutableStateFlow("")
    val time: StateFlow<String> = _time

    private val _selectedStartTimeBlock = MutableStateFlow(StartTimeBlock())
    val selectedStartTimeBlock: StateFlow<StartTimeBlock> = _selectedStartTimeBlock

    private var availabilityType1Blocks: MutableList<Block> = mutableListOf<Block>()
    private var availabilityType2Blocks: MutableList<Block> = mutableListOf<Block>()
    private var availabilityType3Blocks: MutableList<Block> = mutableListOf<Block>()
    private var availabilityType4Blocks: MutableList<Block> = mutableListOf<Block>()

    private var selectedAvailabilityType = ""

    init {
        _duration.value = schedulesRepository.duration
        _selectedStartTimeBlock.value = StartTimeBlock(
            schedulesRepository.conflict.value.id,
            schedulesRepository.conflict.value.startTime,
            schedulesRepository.conflict.value.endTime
        )
        viewModelScope.launch {
            delay(1000)
            updateTimeOptions()
        }
    }

    fun setupDurationAndSchedulingMethods() {
        _durationOptions.value = listOf<Int>(30, 45, 60, 90)
    }

    suspend fun fetchUser(userId: String, availabilityType: String) {

        selectedAvailabilityType = availabilityType

        setupDurationAndSchedulingMethods()

        schedulesRepository.user?.availabilityType1.let {
            for (block in it ?: emptyList()) {
                val startAndEnd = block.split("->")
                val start = startAndEnd.getOrNull(0) ?: continue
                val end = startAndEnd.getOrNull(1)?: continue
                availabilityType1Blocks.add(
                    Block(UUID.randomUUID().toString(), start.toLocalDateTime().toDate(), start.toLocalDateTime(), end.toLocalDateTime())
                )
            }
        }
    }

    fun getGroups(blocks: List<Block>): Pair<Map<String, MutableList<Block>>, List<String>> {
        val dates = mutableSetOf<String>()
        val dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT)

        for (block in blocks) {
            val start = block.startTime.format(dateFormatter)
            dates.add(start)
        }

        val sortedDates = dates.sorted()
        val result = sortedDates.associateWith { mutableListOf<Block>() }.toMutableMap()

        for (block in blocks) {
            val start = block.startTime.format(dateFormatter)
            result[start]?.add(block)
        }

        return Pair(result, sortedDates)
    }

    fun getOptions(duration: Int, blocks: List<Block>, masks: List<Block>): List<StartTimeBlockSection> {
        val adjustedDuration = duration
        val (blockGroups, sortedDates) = getGroups(blocks)
        val (maskGroups, _) = getGroups(masks)
        val resultMap = sortedDates.associateWith { mutableListOf<List<StartTimeBlock>>() }.toMutableMap()

        for (date in sortedDates) {
            blockGroups[date]?.forEach { block ->
                val blockResult = mutableListOf<StartTimeBlock>()
                var start = block.startTime
                val end = block.endTime

                while (start.isBefore(end) || start.isEqual(end)) {
                    val projection = start.plusMinutes(adjustedDuration.toLong())
                    if (projection.isBefore(end) || projection.isEqual(end)) {
                        blockResult.add(StartTimeBlock(UUID.randomUUID().toString(), start, projection))
                    }
                    start = projection
                }

                resultMap[date]?.add(blockResult)
            }
        }

        for (date in sortedDates) {
            resultMap[date]?.forEach { times ->
                times.forEach { time ->
                    var isSelectable = true
                    maskGroups[date]?.forEach { mask ->
                        if (!isSelectable) return@forEach

                        val adjustedMaskStart = mask.startTime.minusMinutes(5)
                        val adjustedMaskEnd = mask.endTime.plusMinutes(5)
                        if (time.start.isBefore(adjustedMaskEnd) && time.end.isAfter(adjustedMaskStart)) {
                            isSelectable = false
                        }
                    }
                    time.isSelectable = isSelectable
                }
            }
        }

        var results = emptyList<StartTimeBlockSection>()

        for (date in sortedDates) {
            val times = resultMap[date] ?: emptyList()
            results = results + StartTimeBlockSection(date, times)
        }

        return results
    }

    fun onDurationChange(duration: Int) {
        _duration.value = duration
        schedulesRepository.duration = duration
        updateTimeOptions()
    }

    fun updateTimeOptions() {
        if (_duration.value == 0) return

        when (selectedAvailabilityType) {
            "1" -> {
                _timeOptions.value = getOptions(_duration.value, availabilityType1Blocks, emptyList())
            }
            "2" -> {
                _timeOptions.value = getOptions(_duration.value, availabilityType2Blocks, emptyList())
            }
            "3" -> {
                _timeOptions.value = getOptions(_duration.value, availabilityType3Blocks, emptyList())
            }
            "4" -> {
                _timeOptions.value = getOptions(_duration.value, availabilityType4Blocks, emptyList())
            }
        }
    }

    fun selectTime(time: StartTimeBlock) {
        schedulesRepository.selectTime(time)
        _selectedStartTimeBlock.value = time
    }
}
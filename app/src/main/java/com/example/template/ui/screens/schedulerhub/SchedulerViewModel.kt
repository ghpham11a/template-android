package com.example.template.ui.screens.schedulerhub

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.template.models.Block
import com.example.template.networking.APIGateway
import com.example.template.repositories.UserRepository
import com.example.template.utils.toDate
import com.example.template.utils.toLocalDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import java.util.*

val DATE_FORMAT = "yyyy-MM-dd"
val DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm"
val TIME_FORMAT = "HH:mm"

class Time(var start: LocalDateTime, var end: LocalDateTime) {
    var isSelectable: Boolean = true

    override fun toString(): String {
        val timeFormatter = DateTimeFormatter.ofPattern(TIME_FORMAT)
        return this.start.format(timeFormatter)
    }
}

data class SchedulerOption(
    val date: String,
    val times: List<List<Time>>
)

@HiltViewModel
class SchedulerViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _durationOptions = MutableStateFlow(emptyList<Int>())
    val durationOptions: StateFlow<List<Int>> = _durationOptions

    private val _duration = MutableStateFlow(0)
    val duration: StateFlow<Int> = _duration

    private val _schedulingOptions = MutableStateFlow(emptyList<Int>())
    val schedulingOptions: StateFlow<List<Int>> = _schedulingOptions

    private val _schedulingMethod = MutableStateFlow(0)
    val schedulingMethod: StateFlow<Int> = _schedulingMethod

    private val _timeOptions = MutableStateFlow(emptyList<SchedulerOption>())
    val timeOptions: StateFlow<List<SchedulerOption>> = _timeOptions

    private val _time = MutableStateFlow("")
    val time: StateFlow<String> = _time

    private var availabilityType1Blocks: MutableList<Block> = mutableListOf<Block>()
    private var availabilityType2Blocks: MutableList<Block> = mutableListOf<Block>()
    private var availabilityType3Blocks: MutableList<Block> = mutableListOf<Block>()
    private var availabilityType4Blocks: MutableList<Block> = mutableListOf<Block>()

    var selectedAvailabilityType = ""

    fun setupDurationAndSchedulingMethods() {
        _durationOptions.value = listOf<Int>(30, 45, 60, 90)
        _schedulingOptions.value = listOf<Int>(1, 2, 3)
    }

    suspend fun fetchUser(userId: String, availabilityType: String) {

        selectedAvailabilityType = availabilityType

        setupDurationAndSchedulingMethods()

        val response = APIGateway.api.publicReadUser(
            APIGateway.buildAuthorizedHeaders(userRepository.idToken ?: ""),
            userId
        )
        if (response.isSuccessful) {

            response.body()?.availabilityType1.let {
                for (block in it ?: emptyList()) {
                    val startAndEnd = block.split("->")
                    val start = startAndEnd.getOrNull(0) ?: continue
                    val end = startAndEnd.getOrNull(1)?: continue
                    availabilityType1Blocks.add(
                        Block(UUID.randomUUID().toString(), start.toLocalDateTime().toDate(), start.toLocalDateTime(), end.toLocalDateTime())
                    )
                }
            }
        } else {

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

    fun getOptions(duration: Int, blocks: List<Block>, masks: List<Block>): List<SchedulerOption> {
        val adjustedDuration = duration
        val (blockGroups, sortedDates) = getGroups(blocks)
        val (maskGroups, _) = getGroups(masks)
        val resultMap = sortedDates.associateWith { mutableListOf<List<Time>>() }.toMutableMap()

        for (date in sortedDates) {
            blockGroups[date]?.forEach { block ->
                val blockResult = mutableListOf<Time>()
                var start = block.startTime
                val end = block.endTime

                while (start.isBefore(end) || start.isEqual(end)) {
                    val projection = start.plusMinutes(adjustedDuration.toLong())
                    if (projection.isBefore(end) || projection.isEqual(end)) {
                        blockResult.add(Time(start, projection))
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

        var results = emptyList<SchedulerOption>()

        for (date in sortedDates) {
            val times = resultMap[date] ?: emptyList()
            results = results + SchedulerOption(date, times)
        }

        return results
    }

    fun onDurationChange(duration: Int) {
        _duration.value = duration
        updateTimeOptions()
    }

    fun onSchedulingMethodChange(schedulingMethod: Int) {
        _schedulingMethod.value = schedulingMethod
        updateTimeOptions()
    }

    fun updateTimeOptions() {
        if (_duration.value == 0 || _schedulingMethod.value == 0) return

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
}
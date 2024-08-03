package com.example.template.repositories

import android.content.SharedPreferences
import com.example.template.models.Block
import com.example.template.models.ReadUserPublicResponse
import com.example.template.ui.screens.schedulerhub.models.StartTimeBlock
import com.example.template.utils.toDate
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject


class SchedulesRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    private val _conflict = MutableStateFlow(Block("IGNORE", "IGNORE", LocalDateTime.now(), LocalDateTime.now()))
    val conflict: MutableStateFlow<Block> = _conflict


    var duration: Int = 0
    var user: ReadUserPublicResponse? = null

    fun selectTime(time: StartTimeBlock) {
        _conflict.value = Block(id = UUID.randomUUID().toString(), time.start.toDate(), time.start, time.end)
    }
}
package com.example.template.ui.screens.availability

import androidx.lifecycle.ViewModel
import com.example.template.models.Block
import com.example.template.models.ProxyCallEvent
import com.example.template.repositories.UserRepository
import com.example.template.utils.toLocalDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AvailabilityScreenViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _displayedBlocks = MutableStateFlow<List<Block>>(emptyList())
    val displayedBlocks: StateFlow<List<Block>> = _displayedBlocks

    fun addBlock() {
        val blocks = _displayedBlocks.value.toMutableList()
        blocks.add(Block("2024-07-21 13:30".toLocalDateTime(), "2024-07-21 17:00".toLocalDateTime()))
        _displayedBlocks.value = blocks
    }

}
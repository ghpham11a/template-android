package com.example.template.ui.screens.publicprofile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.R
import com.example.template.models.Tag
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PublicProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _isEditable = MutableStateFlow(false)
    val isEditable: StateFlow<Boolean> = _isEditable

    val _schoolName = MutableStateFlow("")
    val schoolName: StateFlow<String> = _schoolName

    private val _userTags = MutableStateFlow(emptyList<Tag>())
    val userTags: StateFlow<List<Tag>> = _userTags

    val _isScreenLoading = MutableStateFlow(true)
    val isScreenLoading: StateFlow<Boolean> = _isScreenLoading

    private fun tagTitleFromId(id: Int): Int {
        return when (id) {
            1 -> R.string.tag_alpha
            2 ->  R.string.tag_bravo
            3 ->  R.string.tag_charlie
            4 ->  R.string.tag_delta
            5 -> R.string.tag_echo
            else ->  -1
        }
    }

    fun checkIfEditable(userId: String) {
        viewModelScope.launch {
            _isEditable.value = userRepository.userId == userId
        }
    }

    fun fetchUser(context: Context, userId: String) {
        viewModelScope.launch {
            val response = userRepository.publicReadUser(userId)
            if (response != null && response.isSuccessful) {
                // Handle the successful response
                val user = response.body()
                _schoolName.value = user?.schoolName ?: ""
                _userTags.value = user?.tags?.map { Tag(id = it, title = context.getString(tagTitleFromId(it ?: -1))) } ?: emptyList()
            } else {
                // Handle the error
                println("Error reading user")
            }
            _isScreenLoading.value = false
        }
    }


}
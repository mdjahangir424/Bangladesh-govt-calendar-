package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.PreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsUiState(
    val language: String = "bn",
    val themeMode: String = "system",
    val hijriOffset: Int = 0,
    val notificationsEnabled: Boolean = true,
    val notifyOnDay: Boolean = true,
    val notifyDayBefore: Boolean = true
)

class SettingsViewModel(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesRepository.languageFlow.collect { lang ->
                _uiState.update { it.copy(language = lang) }
            }
        }
        viewModelScope.launch {
            preferencesRepository.themeFlow.collect { theme ->
                _uiState.update { it.copy(themeMode = theme) }
            }
        }
        viewModelScope.launch {
            preferencesRepository.hijriOffsetFlow.collect { offset ->
                _uiState.update { it.copy(hijriOffset = offset) }
            }
        }
        viewModelScope.launch {
            preferencesRepository.notificationsEnabledFlow.collect { enabled ->
                _uiState.update { it.copy(notificationsEnabled = enabled) }
            }
        }
        viewModelScope.launch {
            preferencesRepository.notifyOnDayFlow.collect { enabled ->
                _uiState.update { it.copy(notifyOnDay = enabled) }
            }
        }
        viewModelScope.launch {
            preferencesRepository.notifyDayBeforeFlow.collect { enabled ->
                _uiState.update { it.copy(notifyDayBefore = enabled) }
            }
        }
    }

    fun setLanguage(language: String) {
        viewModelScope.launch {
            preferencesRepository.setLanguage(language)
        }
    }

    fun setTheme(theme: String) {
        viewModelScope.launch {
            preferencesRepository.setTheme(theme)
        }
    }

    fun setHijriOffset(offset: Int) {
        viewModelScope.launch {
            preferencesRepository.setHijriOffset(offset)
        }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setNotificationsEnabled(enabled)
        }
    }

    fun setNotifyOnDay(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setNotifyOnDay(enabled)
        }
    }

    fun setNotifyDayBefore(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setNotifyDayBefore(enabled)
        }
    }

    fun resetPreferences() {
        viewModelScope.launch {
            preferencesRepository.resetPreferences()
        }
    }
}

class SettingsViewModelFactory(
    private val preferencesRepository: PreferencesRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(preferencesRepository) as T
    }
}

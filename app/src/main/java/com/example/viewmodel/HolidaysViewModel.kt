package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.HolidayRepository
import com.example.model.Holiday
import com.example.model.HolidayCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

data class HolidaysUiState(
    val selectedYear: Int = LocalDate.now().year,
    val availableYears: List<Int> = listOf(2025, 2026, 2027),
    val selectedCategory: HolidayCategory? = null,
    val searchQuery: String = "",
    val holidays: List<Holiday> = emptyList(),
    val isYearVerified: Boolean = true,
    val selectedHoliday: Holiday? = null
)

class HolidaysViewModel(
    private val holidayRepository: HolidayRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HolidaysUiState())
    val uiState: StateFlow<HolidaysUiState> = _uiState.asStateFlow()

    init {
        val years = holidayRepository.getAvailableYears()
        val defaultYear = if (years.contains(2026)) 2026 else (years.firstOrNull() ?: 2026)
        _uiState.update {
            it.copy(
                availableYears = if (years.isNotEmpty()) years else listOf(2025, 2026, 2027),
                selectedYear = defaultYear
            )
        }
        refreshHolidays()
    }

    fun setYear(year: Int) {
        _uiState.update { it.copy(selectedYear = year) }
        refreshHolidays()
    }

    fun setCategory(category: HolidayCategory?) {
        _uiState.update { it.copy(selectedCategory = category) }
        refreshHolidays()
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        refreshHolidays()
    }

    fun selectHoliday(holiday: Holiday?) {
        _uiState.update { it.copy(selectedHoliday = holiday) }
    }

    private fun refreshHolidays() {
        val state = _uiState.value
        val isVerified = holidayRepository.isYearVerified(state.selectedYear)
        val rawHolidays = holidayRepository.searchHolidays(state.searchQuery, state.selectedYear)

        val filtered = if (state.selectedCategory != null) {
            rawHolidays.filter { it.category == state.selectedCategory }
        } else {
            rawHolidays
        }

        _uiState.update {
            it.copy(
                holidays = filtered,
                isYearVerified = isVerified
            )
        }
    }
}

class HolidaysViewModelFactory(
    private val holidayRepository: HolidayRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HolidaysViewModel(holidayRepository) as T
    }
}

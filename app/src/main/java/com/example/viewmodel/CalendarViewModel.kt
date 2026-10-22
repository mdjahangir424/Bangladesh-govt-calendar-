package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.calendar.BengaliCalendarConverter
import com.example.calendar.HijriCalendarConverter
import com.example.data.HolidayRepository
import com.example.data.PreferencesRepository
import com.example.model.CalendarDayInfo
import com.example.model.Holiday
import com.example.util.AppConstants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

data class CalendarUiState(
    val today: LocalDate = LocalDate.now(ZoneId.of(AppConstants.DHAKA_ZONE_ID)),
    val selectedDate: LocalDate = LocalDate.now(ZoneId.of(AppConstants.DHAKA_ZONE_ID)),
    val viewMonth: YearMonth = YearMonth.now(ZoneId.of(AppConstants.DHAKA_ZONE_ID)),
    val daysInGrid: List<CalendarDayInfo> = emptyList(),
    val todayInfo: CalendarDayInfo? = null,
    val selectedDayInfo: CalendarDayInfo? = null,
    val nextHoliday: Pair<Holiday, Long>? = null,
    val upcomingHolidays: List<Holiday> = emptyList(),
    val hijriOffset: Int = 0
)

class CalendarViewModel(
    private val holidayRepository: HolidayRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesRepository.hijriOffsetFlow.collectLatest { offset ->
                _uiState.update { it.copy(hijriOffset = offset) }
                refreshCalendar()
            }
        }
        refreshToday()
    }

    fun refreshToday() {
        val today = LocalDate.now(ZoneId.of(AppConstants.DHAKA_ZONE_ID))
        _uiState.update { current ->
            current.copy(
                today = today,
                selectedDate = today,
                viewMonth = YearMonth.from(today)
            )
        }
        refreshCalendar()
    }

    fun selectDate(date: LocalDate) {
        val hijriOffset = _uiState.value.hijriOffset
        val holidays = holidayRepository.getHolidaysForDate(date)
        val selectedInfo = CalendarDayInfo(
            date = date,
            banglaDate = BengaliCalendarConverter.convert(date),
            hijriDate = HijriCalendarConverter.convert(date, hijriOffset),
            isToday = date == _uiState.value.today,
            isCurrentMonth = date.month == _uiState.value.viewMonth.month,
            holidays = holidays
        )
        _uiState.update { it.copy(selectedDate = date, selectedDayInfo = selectedInfo) }
    }

    fun changeViewMonth(delta: Long) {
        val newMonth = _uiState.value.viewMonth.plusMonths(delta)
        _uiState.update { it.copy(viewMonth = newMonth) }
        refreshCalendar()
    }

    fun setViewYearMonth(year: Int, month: Int) {
        val newMonth = YearMonth.of(year, month)
        _uiState.update { it.copy(viewMonth = newMonth) }
        refreshCalendar()
    }

    fun jumpToToday() {
        val today = _uiState.value.today
        _uiState.update {
            it.copy(
                selectedDate = today,
                viewMonth = YearMonth.from(today)
            )
        }
        refreshCalendar()
    }

    private fun refreshCalendar() {
        val state = _uiState.value
        val today = state.today
        val viewMonth = state.viewMonth
        val offset = state.hijriOffset

        // Today info
        val todayHolidays = holidayRepository.getHolidaysForDate(today)
        val todayInfo = CalendarDayInfo(
            date = today,
            banglaDate = BengaliCalendarConverter.convert(today),
            hijriDate = HijriCalendarConverter.convert(today, offset),
            isToday = true,
            isCurrentMonth = today.month == viewMonth.month && today.year == viewMonth.year,
            holidays = todayHolidays
        )

        // Selected day info
        val selectedHolidays = holidayRepository.getHolidaysForDate(state.selectedDate)
        val selectedInfo = CalendarDayInfo(
            date = state.selectedDate,
            banglaDate = BengaliCalendarConverter.convert(state.selectedDate),
            hijriDate = HijriCalendarConverter.convert(state.selectedDate, offset),
            isToday = state.selectedDate == today,
            isCurrentMonth = state.selectedDate.month == viewMonth.month && state.selectedDate.year == viewMonth.year,
            holidays = selectedHolidays
        )

        // Generate grid days:
        // Week starts on Friday for Bangladesh convention (Friday, Saturday, Sunday, Monday, Tuesday, Wednesday, Thursday)
        val firstDayOfMonth = viewMonth.atDay(1)
        val lastDayOfMonth = viewMonth.atEndOfMonth()

        // Days offset from Friday (Friday = 0, Saturday = 1, ... Thursday = 6)
        val firstDayDayOfWeek = firstDayOfMonth.dayOfWeek
        val startPaddingDays = when (firstDayDayOfWeek) {
            DayOfWeek.FRIDAY -> 0
            DayOfWeek.SATURDAY -> 1
            DayOfWeek.SUNDAY -> 2
            DayOfWeek.MONDAY -> 3
            DayOfWeek.TUESDAY -> 4
            DayOfWeek.WEDNESDAY -> 5
            DayOfWeek.THURSDAY -> 6
        }

        val grid = mutableListOf<CalendarDayInfo>()

        // Preceding month padding
        val gridStartDate = firstDayOfMonth.minusDays(startPaddingDays.toLong())
        var cursor = gridStartDate

        // Total slots should be multiple of 7 (usually 35 or 42)
        val totalDaysNeeded = if (startPaddingDays + viewMonth.lengthOfMonth() > 35) 42 else 35

        for (i in 0 until totalDaysNeeded) {
            val isCurrentMonth = cursor.month == viewMonth.month && cursor.year == viewMonth.year
            val dayHolidays = holidayRepository.getHolidaysForDate(cursor)
            grid.add(
                CalendarDayInfo(
                    date = cursor,
                    banglaDate = BengaliCalendarConverter.convert(cursor),
                    hijriDate = HijriCalendarConverter.convert(cursor, offset),
                    isToday = cursor == today,
                    isCurrentMonth = isCurrentMonth,
                    holidays = dayHolidays
                )
            )
            cursor = cursor.plusDays(1)
        }

        // Upcoming holidays
        val nextHoliday = holidayRepository.getNextUpcomingHoliday(today)
        val upcoming = holidayRepository.getHolidaysForYear(today.year)
            .filter { holiday ->
                try {
                    val start = LocalDate.parse(holiday.startDate)
                    !start.isBefore(today)
                } catch (e: Exception) {
                    false
                }
            }
            .take(6)

        _uiState.update {
            it.copy(
                daysInGrid = grid,
                todayInfo = todayInfo,
                selectedDayInfo = selectedInfo,
                nextHoliday = nextHoliday,
                upcomingHolidays = upcoming
            )
        }
    }
}

class CalendarViewModelFactory(
    private val holidayRepository: HolidayRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CalendarViewModel(holidayRepository, preferencesRepository) as T
    }
}

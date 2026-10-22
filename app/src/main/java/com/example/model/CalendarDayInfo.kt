package com.example.model

import java.time.DayOfWeek
import java.time.LocalDate

data class CalendarDayInfo(
    val date: LocalDate,
    val banglaDate: BanglaDate,
    val hijriDate: HijriDate,
    val isToday: Boolean,
    val isCurrentMonth: Boolean,
    val holidays: List<Holiday> = emptyList()
) {
    val isFriday: Boolean get() = date.dayOfWeek == DayOfWeek.FRIDAY
    val isSaturday: Boolean get() = date.dayOfWeek == DayOfWeek.SATURDAY
    val isWeekend: Boolean get() = isFriday || isSaturday
    val hasHoliday: Boolean get() = holidays.isNotEmpty()

    val primaryHoliday: Holiday? get() = holidays.firstOrNull()

    val hasGeneralHoliday: Boolean
        get() = holidays.any { it.category == HolidayCategory.GENERAL }

    val hasExecutiveHoliday: Boolean
        get() = holidays.any { it.category == HolidayCategory.EXECUTIVE }

    val hasObservance: Boolean
        get() = holidays.any { it.category == HolidayCategory.OBSERVANCE }
}

package com.example.calendar

import com.example.model.BanglaDate
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * Official Bangladesh Bengali Calendar Converter.
 *
 * Implements the revised Bengali calendar adopted by the Bangla Academy
 * and the Government of Bangladesh (revised in 2019):
 * - The first 6 months (Boishakh to Ashwin) have 31 days each.
 * - The next 5 months (Kartik, Ogrohayon, Poush, Magh, Choitro) have 30 days each.
 * - Falgun has 29 days in a standard year, and 30 days in a Gregorian leap year.
 * - Pahela Baishakh always falls on April 14.
 *
 * Supported Date Range: Any valid java.time.LocalDate (from 1900 onwards).
 */
object BengaliCalendarConverter {

    private val monthNamesBn = listOf(
        "বৈশাখ", "জ্যৈষ্ঠ", "আষাঢ়", "শ্রাবণ", "ভাদ্র", "আশ্বিন",
        "কার্তিক", "অগ্রহায়ণ", "পৌষ", "মাঘ", "ফাল্গুন", "চৈত্র"
    )

    private val monthNamesEn = listOf(
        "Boishakh", "Jaishtho", "Asharh", "Srabon", "Bhadro", "Ashwin",
        "Kartik", "Ogrohayon", "Poush", "Magh", "Falgun", "Choitro"
    )

    private val seasonNamesBn = listOf(
        "গ্রীষ্ম", "বর্ষা", "শরৎ", "হেমন্ত", "শীত", "বসন্ত"
    )

    private val seasonNamesEn = listOf(
        "Summer", "Monsoon", "Autumn", "Late Autumn", "Winter", "Spring"
    )

    /**
     * Converts a Gregorian LocalDate to a Bengali Date according to the
     * Bangladesh national standard.
     */
    fun convert(date: LocalDate): BanglaDate {
        val year = date.year
        val isCurrentYearLeap = date.isLeapYear

        // Determine Bengali Year
        val banglaYear = if (date.monthValue > 4 || (date.monthValue == 4 && date.dayOfMonth >= 14)) {
            year - 593
        } else {
            year - 594
        }

        // Calculate month lengths for the Bengali year
        // In the Bengali year, Falgun occurs in the Gregorian calendar's February.
        // If the date falls between Jan 1 and Apr 13, Falgun is in `year`.
        // If the date falls between Apr 14 and Dec 31, Falgun will be in `year + 1`.
        val falgunYear = if (date.monthValue > 4 || (date.monthValue == 4 && date.dayOfMonth >= 14)) {
            year + 1
        } else {
            year
        }
        val isFalgunLeapYear = LocalDate.of(falgunYear, 1, 1).isLeapYear
        val falgunDays = if (isFalgunLeapYear) 30 else 29

        val monthDays = intArrayOf(
            31, // 1. Boishakh
            31, // 2. Jaishtho
            31, // 3. Asharh
            31, // 4. Srabon
            31, // 5. Bhadro
            31, // 6. Ashwin
            30, // 7. Kartik
            30, // 8. Ogrohayon
            30, // 9. Poush
            30, // 10. Magh
            falgunDays, // 11. Falgun
            30  // 12. Choitro
        )

        // Find days elapsed since 1st Boishakh of this Bengali year
        val boishakh1 = if (date.monthValue > 4 || (date.monthValue == 4 && date.dayOfMonth >= 14)) {
            LocalDate.of(year, 4, 14)
        } else {
            LocalDate.of(year - 1, 4, 14)
        }

        val daysPassed = ChronoUnit.DAYS.between(boishakh1, date).toInt()

        var accumulatedDays = 0
        var monthIndex = 0 // 0 to 11
        var dayInMonth = 1

        for (i in monthDays.indices) {
            val length = monthDays[i]
            if (daysPassed < accumulatedDays + length) {
                monthIndex = i
                dayInMonth = daysPassed - accumulatedDays + 1
                break
            }
            accumulatedDays += length
        }

        val seasonIndex = (monthIndex / 2).coerceIn(0, 5)

        return BanglaDate(
            day = dayInMonth,
            monthIndex = monthIndex + 1,
            monthNameBn = monthNamesBn[monthIndex],
            monthNameEn = monthNamesEn[monthIndex],
            year = banglaYear,
            seasonBn = seasonNamesBn[seasonIndex],
            seasonEn = seasonNamesEn[seasonIndex]
        )
    }
}

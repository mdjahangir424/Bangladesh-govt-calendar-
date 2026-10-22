package com.example.util

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month

object BengaliDateFormatter {

    private val bengaliDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')

    fun toBengaliDigits(number: Int): String {
        return toBengaliDigits(number.toString())
    }

    fun toBengaliDigits(number: Long): String {
        return toBengaliDigits(number.toString())
    }

    fun toBengaliDigits(text: String): String {
        val sb = StringBuilder()
        for (ch in text) {
            if (ch in '0'..'9') {
                sb.append(bengaliDigits[ch - '0'])
            } else {
                sb.append(ch)
            }
        }
        return sb.toString()
    }

    fun getWeekdayNameBn(dayOfWeek: DayOfWeek): String {
        return when (dayOfWeek) {
            DayOfWeek.FRIDAY -> "শুক্রবার"
            DayOfWeek.SATURDAY -> "শনিবার"
            DayOfWeek.SUNDAY -> "রবিবার"
            DayOfWeek.MONDAY -> "সোমবার"
            DayOfWeek.TUESDAY -> "মঙ্গলবার"
            DayOfWeek.WEDNESDAY -> "বুধবার"
            DayOfWeek.THURSDAY -> "বৃহস্পতিবার"
        }
    }

    fun getWeekdayShortBn(dayOfWeek: DayOfWeek): String {
        return when (dayOfWeek) {
            DayOfWeek.FRIDAY -> "শুক্র"
            DayOfWeek.SATURDAY -> "শনি"
            DayOfWeek.SUNDAY -> "রবি"
            DayOfWeek.MONDAY -> "সোম"
            DayOfWeek.TUESDAY -> "মঙ্গল"
            DayOfWeek.WEDNESDAY -> "বুধ"
            DayOfWeek.THURSDAY -> "বৃহ"
        }
    }

    fun getWeekdayNameEn(dayOfWeek: DayOfWeek): String {
        return when (dayOfWeek) {
            DayOfWeek.FRIDAY -> "Friday"
            DayOfWeek.SATURDAY -> "Saturday"
            DayOfWeek.SUNDAY -> "Sunday"
            DayOfWeek.MONDAY -> "Monday"
            DayOfWeek.TUESDAY -> "Tuesday"
            DayOfWeek.WEDNESDAY -> "Wednesday"
            DayOfWeek.THURSDAY -> "Thursday"
        }
    }

    fun getWeekdayShortEn(dayOfWeek: DayOfWeek): String {
        return when (dayOfWeek) {
            DayOfWeek.FRIDAY -> "Fri"
            DayOfWeek.SATURDAY -> "Sat"
            DayOfWeek.SUNDAY -> "Sun"
            DayOfWeek.MONDAY -> "Mon"
            DayOfWeek.TUESDAY -> "Tue"
            DayOfWeek.WEDNESDAY -> "Wed"
            DayOfWeek.THURSDAY -> "Thu"
        }
    }

    fun getGregorianMonthBn(month: Month): String {
        return when (month) {
            Month.JANUARY -> "জানুয়ারি"
            Month.FEBRUARY -> "ফেব্রুয়ারি"
            Month.MARCH -> "মার্চ"
            Month.APRIL -> "এপ্রিল"
            Month.MAY -> "মে"
            Month.JUNE -> "জুন"
            Month.JULY -> "জুলাই"
            Month.AUGUST -> "আগস্ট"
            Month.SEPTEMBER -> "সেপ্টেম্বর"
            Month.OCTOBER -> "অক্টোবর"
            Month.NOVEMBER -> "নভেম্বর"
            Month.DECEMBER -> "ডিসেম্বর"
        }
    }

    fun formatGregorianBn(date: LocalDate): String {
        val day = toBengaliDigits(date.dayOfMonth)
        val month = getGregorianMonthBn(date.month)
        val year = toBengaliDigits(date.year)
        val weekday = getWeekdayNameBn(date.dayOfWeek)
        return "$weekday, $day $month $year"
    }

    fun formatGregorianEn(date: LocalDate): String {
        val weekday = getWeekdayNameEn(date.dayOfWeek)
        return "$weekday, ${date.dayOfMonth} ${date.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${date.year}"
    }
}

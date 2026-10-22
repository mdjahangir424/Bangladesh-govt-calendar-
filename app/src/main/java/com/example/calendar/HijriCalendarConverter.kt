package com.example.calendar

import com.example.model.HijriDate
import java.time.LocalDate
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField

/**
 * Hijri (Islamic Lunar) Calendar Converter.
 *
 * Uses Java time HijrahDate (Umm al-Qura standard) along with a customizable
 * day-offset (-2 to +2 days) to conform with local Bangladesh moon sighting
 * committee (জাতীয় চাঁদ দেখা কমিটি) announcements.
 *
 * Clearly marks output as estimated unless verified against astronomical or gazette sources.
 */
object HijriCalendarConverter {

    private val monthNamesBn = listOf(
        "মুহাররম", "সফর", "রবিউল আউয়াল", "রবিউস সানি",
        "জমাদিউল আউয়াল", "জমাদিউস সানি", "রজব", "শাবান",
        "রমজান", "শাওয়াল", "জিলকদ", "জিলহজ্ব"
    )

    private val monthNamesEn = listOf(
        "Muharram", "Safar", "Rabi' al-Awwal", "Rabi' al-Thani",
        "Jumada al-Awwal", "Jumada al-Thani", "Rajab", "Sha'ban",
        "Ramadan", "Shawwal", "Dhu al-Qi'dah", "Dhu al-Hijjah"
    )

    /**
     * Converts a LocalDate to HijriDate with an optional day adjustment
     * (e.g. -1, 0, +1 day) representing Bangladesh local moon visibility.
     */
    fun convert(date: LocalDate, adjustmentDays: Int = 0): HijriDate {
        return try {
            val adjustedDate = if (adjustmentDays != 0) {
                date.plusDays(adjustmentDays.toLong())
            } else {
                date
            }

            val hijrahDate = HijrahDate.from(adjustedDate)
            val day = hijrahDate.get(ChronoField.DAY_OF_MONTH)
            val monthIndex = hijrahDate.get(ChronoField.MONTH_OF_YEAR) // 1..12
            val year = hijrahDate.get(ChronoField.YEAR_OF_ERA)

            val safeMonthIndex = monthIndex.coerceIn(1, 12) - 1

            HijriDate(
                day = day,
                monthIndex = monthIndex,
                monthNameBn = monthNamesBn[safeMonthIndex],
                monthNameEn = monthNamesEn[safeMonthIndex],
                year = year,
                isEstimated = true
            )
        } catch (e: Exception) {
            // Algorithmic fallback if chrono exception occurs
            calculateFallbackHijri(date, adjustmentDays)
        }
    }

    /**
     * Tabular Islamic calendar calculation fallback for extreme dates or edge cases.
     */
    private fun calculateFallbackHijri(date: LocalDate, adjustmentDays: Int): HijriDate {
        val jd = date.toEpochDay() + 2440588 + adjustmentDays // Julian Day Number
        val l = jd - 1948440 + 10632
        val n = ((l - 1) / 10631)
        val lRemainder = l - 10631 * n + 354
        val j = (((10985 - lRemainder) / 5316)) * (((50 * lRemainder) / 17719)) +
                ((lRemainder / 5670)) * (((43 * lRemainder) / 15238))
        val lNew = lRemainder - (((30 - j) / 15)) * (((17719 * j) / 50)) -
                ((j / 16)) * (((15238 * j) / 43)) + 29
        val month = ((24 * lNew) / 709)
        val day = (lNew - ((709 * month) / 24)).toInt()
        val year = (30 * n + j - 30).toInt()

        val safeMonth = month.toInt().coerceIn(1, 12)
        val monthIdx = safeMonth - 1

        return HijriDate(
            day = day.coerceIn(1, 30),
            monthIndex = safeMonth,
            monthNameBn = monthNamesBn[monthIdx],
            monthNameEn = monthNamesEn[monthIdx],
            year = year,
            isEstimated = true
        )
    }
}
